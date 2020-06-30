package server;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


import server.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class App
{

	static public SessionFactory sf;

	private static SessionFactory getSessionFactory() throws HibernateException {
		Configuration configuration =new Configuration();
		configuration.addAnnotatedClass(Course.class);
		configuration.addAnnotatedClass(Exam.class);
		configuration.addAnnotatedClass(Grade.class);
		configuration.addAnnotatedClass(Principle.class);
		configuration.addAnnotatedClass(Question.class);
		configuration.addAnnotatedClass(Student.class);
		configuration.addAnnotatedClass(Subject.class);
		configuration.addAnnotatedClass(Teacher.class);
		configuration.addAnnotatedClass(Answer.class);
		configuration.addAnnotatedClass(Request.class);

		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.build();

		return configuration.buildSessionFactory(serviceRegistry);
	}

	public static Session getSession(){
		Session s = sf.openSession();
		s.beginTransaction();
		return s;
	}

	public static void SetUp( )
	{
		sf = getSessionFactory();
		System.out.println("Connected to database");

	}
}
