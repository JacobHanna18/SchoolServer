package il.ac.haifa.cs.sweng.HelloHibernate;

import java.awt.desktop.PrintFilesEvent;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import il.ac.haifa.cs.sweng.HelloHibernate.entities.Course;
import il.ac.haifa.cs.sweng.HelloHibernate.entities.Lecturer;
import il.ac.haifa.cs.sweng.HelloHibernate.entities.Student;

public class App 
{
	private static Session session;
	
	private static SessionFactory getSessionFactory() throws HibernateException {
		Configuration configuration =new Configuration();
		configuration.addAnnotatedClass(Course.class);
		configuration.addAnnotatedClass(Student.class);
		configuration.addAnnotatedClass(Lecturer.class);
		
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.build();
		
		return configuration.buildSessionFactory(serviceRegistry);
	}
	
	private static void initializeData() throws Exception {
		
		Lecturer oren = new Lecturer("Oren", "Weimann");
		Lecturer gadi = new Lecturer("Gadi", "Landau");
		Lecturer rachel = new Lecturer("Rachel", "Kolodny");
		
		session.save(oren);
		session.save(gadi);
		session.save(rachel);
		
		session.flush();
		
		Course dsOren = new Course("Data Structures", oren);
    	Course dsGadi = new Course("Data Structures for non-programmers", gadi);
    	Course osRachel = new Course("Operating Systems", rachel);
    	Course sciProgramming = new Course("Scientific Programming", rachel);
    	
    	session.flush();
    	
    	session.save(dsOren);
    	session.save(dsGadi);
    	session.save(osRachel);
    	session.save(sciProgramming);
    	
    	session.flush();
    	
    	Student haim = new Student("Haim", "Blabla");
    	haim.addCourses(dsOren, osRachel);
    	session.save(haim);
    	
    	Student shlomi = new Student("Shlomi", "Ben Artzi");
    	shlomi.addCourses(dsGadi, osRachel);
    	session.save(shlomi);
    	
    	session.getTransaction().commit();
	}
	
	public static <T> List<T> getAll(Class<T> object) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(object);
		Root<T> rootEntry = criteriaQuery.from(object);
		CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);
		
		TypedQuery<T> allQuery = session.createQuery(allCriteriaQuery);
		return allQuery.getResultList();
	}
	
    public static void main( String[] args )
    {
        try {
        	
        	//Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        	
        	SessionFactory sessionFactory = getSessionFactory();
        	session = sessionFactory.openSession();
        	session.beginTransaction();
        	
        	initializeData();
        	
        	List<Student> students = getAll(Student.class);
        	
        	System.out.println("Students list:");
        	for (Student student : students) {
        		System.out.format("ID: %d, Name: %s %s, Courses:\n",
        				student.getId(), student.getFirstName(),
        				student.getLastName());
        		for (Course course : student.getCourseList()) {
        			System.out.format(" - Name: %s, Lecturer: %s %s\n",
        					course.getName(),
        					course.getLecturer().getFirstName(),
        					course.getLecturer().getLastName());
        		}
        	}
        	
        	List<Course> courses = getAll(Course.class);
        	
        	System.out.println("\n\nCourses list:");
        	for (Course course : courses) {
        		System.out.format("ID: %d, Name: %s, Lecturer: %s %s, Students:\n",
        				course.getId(), course.getName(),
        				course.getLecturer().getFirstName(),
        				course.getLecturer().getLastName());
        		for (Student student : course.getStudents()) {
        			System.out.format(" - Name: %s %s\n",
        					student.getFirstName(), student.getLastName());
        		}
        	}
        	
        	List<Lecturer> lecturers = getAll(Lecturer.class);
        	System.out.println("\n\nLecturers list:");
        	for (Lecturer lecturer : lecturers) {
        		System.out.format("Name: %s %s, Courses:\n",
        				lecturer.getFirstName(), lecturer.getLastName());
        		
        		for (Course course : lecturer.getCourses()) {
        			System.out.format(" - Name: %s\n", course.getName());
        		}
        	}
        	System.out.format("\n\n");
        	System.out.println("Done!");
        	
        	
        } catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
				session.getSessionFactory().close();
			}
		}
    }
}
