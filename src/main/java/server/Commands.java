package server;

import com.google.gson.Gson;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.query.Query;
import server.clientClasses.*;
import server.entities.*;

import org.json.simple.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Commands {
    String currentUser;
    int userType;
    static Gson gson = new Gson();
    String LogIn (String user, String password){
        JSONObject js = new JSONObject();
        clientUser cu = new clientUser();
        cu.id = user;
        cu.role = 0;
        Student s = App.session.get(Student.class,user);
        if(s != null){
            if(s.getPass().equals(password)){
                currentUser = user;
                userType = cu.role = 1;
                cu.name = s.getName();
            }
        }
        Teacher t = App.session.get(Teacher.class,user);
        if(t != null){
            if(t.getPass().equals(password)){
                currentUser = user;
                userType = cu.role = 2;
                cu.name = t.getName();
            }
        }
        Principle p = App.session.get(Principle.class,user);
        if(p != null){
            if(p.getPass().equals(password)){
                currentUser = user;
                userType = cu.role = 3;
                cu.name = p.getName();
            }
        }

        return gson.toJson(cu);
    }

    String teacherSubjectList (String teacherId){
        String hql = "SELECT c.subject FROM Course c WHERE c.teacher = " + teacherId;
        List<Subject> l = listFrom(hql,Subject.class);
        ArrayList<clientSubject> arr = new ArrayList<>();
        for (Subject s : l){
            arr.add(new clientSubject(s.getName(),s.getId()));
        }
        return gson.toJson(arr);
    }

    String getExam (int examID){
        Exam e = App.session.get(Exam.class, examID);

        clientExam exam = new clientExam(e.getId(), e.getTeacher().getName());
        for (Question q : e.getQuestions()){
            exam.questions.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return gson.toJson(exam);
    }

    String examFromCourse (int courseId){
        Course c = App.session.get(Course.class, courseId);

        return getExam(c.getExam().getId());
    }

    void selectExam (int examId, int courseId){
        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        Course c = em.getReference(Course.class,courseId);
        Exam e = em.getReference(Exam.class,examId);
        c.setExam(e);


        em.persist(c);
        em.getTransaction().commit();
        em.close();
    }

    void startExam (int courseId, int accessID, int duration){
        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        Course c = em.getReference(Course.class,courseId);

        c.setStartTime((int) (System.currentTimeMillis() / 1000));
        c.setAccessCode(accessID);
        c.setDuration(duration);

        em.persist(c);
        em.getTransaction().commit();
        em.close();
    }

    String subjectQuestionList (int subjectId){
        String hql = "FROM Question q WHERE q.subject = " + subjectId;
        List<Question> l = listFrom(hql,Question.class);
        ArrayList<clientQuestion> arr = new ArrayList<>();
        for (Question q : l){
            arr.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return gson.toJson(arr);
    }

    String subjectExamList (int subjectId){
        String hql = "FROM Exam e WHERE e.subject = " + subjectId;
        List<Exam> l = listFrom(hql,Exam.class);
        ArrayList<clientExam> arr = new ArrayList<>();
        for (Exam e : l){
            arr.add(new clientExam(e.getId(),e.getTeacher().getName()));
        }
        return gson.toJson(arr);
    }

    void createExam (clientExam e, int subjectID, String teacherID){
        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Exam newExam = new Exam();
        Subject s = em.getReference(Subject.class,subjectID);
        Teacher t = em.getReference(Teacher.class,teacherID);
        newExam.setSubject(s);
        newExam.setTeacher(t);
        for(int id : e.questionIds){
            Question q = em.getReference(Question.class,id);
            newExam.addQuestion(q);
        }
        em.persist(newExam);
        em.getTransaction().commit();
        em.close();
    }

    void createQuestion (clientQuestion q, int subjectID, String teacherID){
        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Question newQ = new Question(q.question,q.right,q.wrong1,q.wrong2,q.wrong3);
        Subject s = em.getReference(Subject.class,subjectID);
        Teacher t = em.getReference(Teacher.class,teacherID);
        newQ.setSubject(s);
        newQ.setTeacher(t);

        em.persist(newQ);
        em.getTransaction().commit();
        em.close();
    }
    /* MAKE TEST FOR THIS */
   // get the asnwers of student in course
    String studnetAnswersOfCourse (int courseId, String studentId){
        String hql = "FROM Answer a WHERE a.course = " + courseId + " AND a.student = " + studentId;
        List<Answer> l = listFrom(hql,Answer.class);
        ArrayList<clientAnswer> arr = new ArrayList<>();
        for (Answer a : l){
            arr.add(new clientAnswer(a.getId()));
        }
        return gson.toJson(arr);
    }
    /* MAKE TEST FOR THIS */
    // get the questions that the teacher wrote
    String questionsOfTeacher (String teacherId){
        String hql = "FROM Question q WHERE q.teacher = " + teacherId ;
        List<Question> l = listFrom(hql,Question.class);
        ArrayList<clientQuestion> arr = new ArrayList<>();
        for (Question q : l){
            arr.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return gson.toJson(arr);
    }
    /* MAKE TEST FOR THIS */
    // get the exams that the teacher created
    String examsFromTeacher (String teacherId){
        String hql = "FROM Exam e WHERE e.teacher = " + teacherId ;
        List<Exam> l = listFrom(hql,Exam.class);
        ArrayList<clientExam> arr = new ArrayList<>();
        for (Exam e : l){
            arr.add(new clientExam(e.getId(),(e.getTeacher().getName())));
        }
        return gson.toJson(arr);
        }

    String coursesOfSubjectTeacher (int subjectId, String teacherId){
        String hql = "FROM Course c WHERE c.subject = " + subjectId + " AND c.teacher = " + teacherId;
        List<Course> l = listFrom(hql,Course.class);
        ArrayList<clientCourse> arr = new ArrayList<>();
        for (Course c : l){
            arr.add(new clientCourse(c.getName(),c.getId()));
        }
        return gson.toJson(arr);
    }

    void newRequest (int courseId, int addedTime, String exp){
        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        Request r = new Request();
        r.setExplaination(exp);
        r.setTimeAdded(addedTime);

        Course c = em.getReference(Course.class,courseId);
        r.setCourse(c);

        em.persist(r);
        em.getTransaction().commit();
        em.close();
    }

    void confirmGrade (String studentID, int courseID){
        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        String hql = "FROM Grade g WHERE g.course = " + courseID + " AND g.student = " + studentID;

        Grade g = em.createQuery(hql,Grade.class).getSingleResult();

        g.setConfirmed(1);

        em.persist(g);
        em.getTransaction().commit();
        em.close();
    }

    void changeAndConfirmGrade (String studentID, int courseID, int newGrade, String reason){

        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        String hql = "FROM Grade g WHERE g.course = " + courseID + " AND g.student = " + studentID;

        Grade g = em.createQuery(hql,Grade.class).getSingleResult();

        g.setChangeReason(reason);
        g.setGrade(newGrade);
        g.setConfirmed(1);

        em.persist(g);
        em.getTransaction().commit();
        em.close();
    }

    String getGrade (String studentID, int courseID){
        String hql = "FROM Grade g WHERE g.course = " + courseID + " AND g.student = " + studentID;

        Grade g = getFirst(hql,Grade.class);
        clientGrade cg = new clientGrade();
        cg.grade = g.getGrade();
        cg.course = new clientCourse(g.getCourse().getName(), g.getCourse().getId());
        cg.student = new clientUser(g.getStudent().getName(), g.getStudent().getId());
        return gson.toJson(cg);
    }

    String getGradesOfCourse (int courseID){
        String hql = "FROM Grade g WHERE g.course = " + courseID;
        List<Grade> gs = listFrom(hql,Grade.class);

        ArrayList <clientGrade> cgs = new ArrayList<>();

        for(Grade g : gs){
            clientGrade cg = new clientGrade();
            cg.grade = g.getGrade();
            cg.course = new clientCourse(g.getCourse().getName(), g.getCourse().getId());
            cg.student = new clientUser(g.getStudent().getName(), g.getStudent().getId());
            cgs.add(cg);
        }

        return gson.toJson(cgs);
    }

    String getGradesOfStudent (String studentID, int confirmed){
        String hql = "FROM Grade g WHERE g.student = " + studentID + (confirmed==1 ? " AND g.confirmed = 1" : "");
        List<Grade> gs = listFrom(hql,Grade.class);

        ArrayList <clientGrade> cgs = new ArrayList<>();

        for(Grade g : gs){
            clientGrade cg = new clientGrade();
            cg.grade = g.getGrade();
            cg.course = new clientCourse(g.getCourse().getName(), g.getCourse().getId());
            cg.student = new clientUser(g.getStudent().getName(), g.getStudent().getId());
            cgs.add(cg);
        }

        return gson.toJson(cgs);
    }


    <T> T getFirst (String hql, Class<T> obj){
        Query<T> query = App.session.createQuery(hql, obj);
        return query.getSingleResult();
    }

    <T> List<T> listFrom (String hql, Class<T> obj ){
        Query<T> query = App.session.createQuery(hql, obj);
        return query.list();
    }

//    String getGrades (String id){
//
//        String hql = "FROM Grade g WHERE g.student = " + id;
//        Query<Grade> query = App.session.createQuery(hql, Grade.class);
//        List<Grade> l = query.list();
//        ArrayList<clientGrade> gs = new ArrayList<>();
//        for (Grade g : l){
//            gs.add(new clientGrade(g.getGrade(),g.getId(),g.getCourse().getSubject().getName()));
//        }
//        return gson.toJson(gs);
//
//    }


}


