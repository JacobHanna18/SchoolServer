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

    String teacherSubjectList (String teacherID){
        String hql = "SELECT c.subject FROM Course c WHERE c.teacher = " + teacherID;
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

    String examFromCourse (int courseID){
        Course c = App.session.get(Course.class, courseID);

        return getExam(c.getExam().getId());
    }

    void selectExam (int examID, int courseID){
        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        Course c = em.getReference(Course.class,courseID);
        Exam e = em.getReference(Exam.class,examID);
        c.setExam(e);


        em.persist(c);
        em.getTransaction().commit();
        em.close();
    }

    String startExam (int courseID, int accessID, int duration, int online){
        String hql = "FROM Course c WHERE c.AccessCode = " + accessID;
        List<Course> l = listFrom(hql,Course.class);
        if(l.size() != 0){
            return gson.toJson(new clientCompletion(false));
        }
        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        Course c = em.getReference(Course.class,courseID);

        c.setStartTime((int) (System.currentTimeMillis() / 1000));
        c.setAccessCode(accessID);
        c.setDuration(duration);
        c.setOnline(online);

        em.persist(c);
        em.getTransaction().commit();
        em.close();
        return gson.toJson(new clientCompletion(true));
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

    String courseAverage (int courseID, int onlyConfirmed){
        String hql = "SELECT avg(grade) FROM Grade g where g.course = " + courseID + (onlyConfirmed==1 ? " AND g.confirmed = 1" : "");
        Double avg = getFirst(hql, Double.class);
        return gson.toJson(avg);
    }

    String studentAverage (String studentID, int onlyConfirmed){
        String hql = "SELECT avg(grade) FROM Grade g where g.student = " + studentID + (onlyConfirmed==1 ? " AND g.confirmed = 1" : "");
        Double avg = getFirst(hql, Double.class);
        return gson.toJson(avg);
    }

    String studentGrade (int courseID, String studentID, int onlyConfirmed){
        String hql = "FROM Answer a WHERE a.course = " + courseID + " AND a.student = " + studentID;
        List<Answer> l = listFrom(hql,Answer.class);

        String hql2 = "FROM Grade g WHERE g.course = " + courseID + " AND g.student = " + studentID;

        Grade g = getFirst(hql2,Grade.class);
        clientGrade cg = new clientGrade();
        if(!(onlyConfirmed == 1 && g.isConfirmed() == 0)){
            cg.grade = g.getGrade();
        }else{
            cg.grade = -1;
        }
        cg.course = new clientCourse(g.getCourse().getName(), g.getCourse().getId(),g.getCourse().getOnline());
        cg.student = new clientUser(g.getStudent().getName(), g.getStudent().getId());

        for (Answer a : l){
            clientAnswer ca = new clientAnswer(a.getAnswer(),a.getQuestion().getId(), a.getCourse().getId());
            Question q = a.getQuestion();
            ca.question = new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName());
            cg.answers.add(ca);

        }
        return gson.toJson(cg);
    }

    String questionsOfTeacher (String teacherID){
        String hql = "FROM Question q WHERE q.teacher = " + teacherID ;
        List<Question> l = listFrom(hql,Question.class);
        ArrayList<clientQuestion> arr = new ArrayList<>();
        for (Question q : l){
            arr.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return gson.toJson(arr);
    }

    String courseStudents (int courseID){
        String hql = "SELECT g.student FROM Grade g WHERE g.course = " + courseID;
        List<Student> l = listFrom(hql,Student.class);
        Course dbc = App.session.get(Course.class, courseID);
        clientCourse e = new clientCourse(dbc.getName(),dbc.getId(),dbc.getOnline());
        for (Student s : l){
            e.students.add(new clientUser(s.getName(),s.getId()));
        }
        return gson.toJson(e);
    }
    String examsFromTeacher (String teacherID){
        String hql = "FROM Exam e WHERE e.teacher = " + teacherID ;
        List<Exam> l = listFrom(hql,Exam.class);
        ArrayList<clientExam> arr = new ArrayList<>();
        for (Exam e : l){
            clientExam ce = new clientExam(e.getId(),(e.getTeacher().getName()));
            for(Question q : e.getQuestions()){
                ce.questionIds.add(q.getId());
            }
            arr.add(ce);
        }
        return gson.toJson(arr);
        }

    String coursesOfSubjectTeacher (int subjectId, String teacherID){
        String hql = "FROM Course c WHERE c.subject = " + subjectId + " AND c.teacher = " + teacherID;
        List<Course> l = listFrom(hql,Course.class);
        ArrayList<clientCourse> arr = new ArrayList<>();
        for (Course c : l){
            arr.add(new clientCourse(c.getName(),c.getId(),c.getOnline()));
        }
        return gson.toJson(arr);
    }

    void newRequest (int courseID, int addedTime, String exp){
        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        Request r = new Request();
        r.setExplaination(exp);
        r.setTimeAdded(addedTime);

        Course c = em.getReference(Course.class,courseID);
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
        cg.course = new clientCourse(g.getCourse().getName(), g.getCourse().getId(),g.getCourse().getOnline());
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
            cg.course = new clientCourse(g.getCourse().getName(), g.getCourse().getId(),g.getCourse().getOnline());
            cg.student = new clientUser(g.getStudent().getName(), g.getStudent().getId());
            cgs.add(cg);
        }

        return gson.toJson(cgs);
    }

    String getGradesOfStudent (String studentID, int onlyConfirmed){
        String hql = "FROM Grade g WHERE g.student = " + studentID;
        List<Grade> gs = listFrom(hql,Grade.class);

        ArrayList <clientGrade> cgs = new ArrayList<>();

        for(Grade g : gs){
            clientGrade cg = new clientGrade();
            cg.online = g.getCourse().getOnline();
            if(!(onlyConfirmed == 1 && g.isConfirmed() == 0)){
                cg.grade = g.getGrade();
            }else{
                cg.grade = -1;
            }
            cg.grade = g.getGrade();
            cg.course = new clientCourse(g.getCourse().getName(), g.getCourse().getId(),g.getCourse().getOnline());
            cg.student = new clientUser(g.getStudent().getName(), g.getStudent().getId());
            cgs.add(cg);
        }

        return gson.toJson(cgs);
    }

    String takeExam (int AccessCode, String studentID){
        String hql = "FROM Course c WHERE c.AccessCode = " + AccessCode;
        Course c = getFirst(hql, Course.class);

        String hql2 = "SELECT g.student FROM Grade g WHERE g.course = " + c.getId() + " AND g.student = " + studentID;
        List<Student> l2 = listFrom(hql2,Student.class);
        if(l2.size() == 0) {
            Student s = App.session.get(Student.class, studentID);
            Grade g = new Grade();
            g.setStudent(s);
            g.setCourse(c);
            App.session.save(g);
            App.session.getTransaction().commit();
        }

        clientExam ce = new clientExam();


        int currentTime = ((int) (System.currentTimeMillis() / 1000));

        if(currentTime - c.getStartTime() > c.getDuration()){
            ce.online = -1;
            return gson.toJson(ce);
        }

        ce.online = c.getOnline();
        Exam e = c.getExam();
        for (Question q : e.getQuestions()){
            ce.questions.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return gson.toJson(ce);
    }
    void submitOnlineExam (ArrayList<clientAnswer> arr, int courseID, String studentID){

        int rightAnswers = 0;
        EntityManager em = App.session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Course c = em.getReference(Course.class, courseID);
        Student s = em.getReference(Student.class, studentID);

        String hql2 = "SELECT g.student FROM Grade g WHERE g.course = " + courseID + " AND g.student = " + studentID;
        List<Student> l2 = listFrom(hql2,Student.class);
        if(l2.size() == 0) {
            Grade g = new Grade();
            g.setStudent(s);
            g.setCourse(c);
            em.persist(g);
        }

        for (clientAnswer ca : arr){
            rightAnswers += (ca.answer == 0 ? 1 : 0);
            Answer newA = new Answer();
            newA.setAnswer(ca.answer);
            Question q = em.getReference(Question.class,ca.questionId);
            newA.setStudent(s);
            newA.setCourse(c);
            newA.setQuestion(q);
            em.persist(newA);
        }



        em.getTransaction().commit();
        em.close();
    }

    String allTeachers (){
        String hql = "SELECT t FROM Teacher t";
        List<Teacher> arr = listFrom(hql,Teacher.class);

        ArrayList<clientUser> ts = new ArrayList<>();

        for( Teacher t : arr){
            ts.add(new clientUser(t.getName(),t.getId(),2));
        }

        return gson.toJson(ts);
    }

    String allStudents (){
        String hql = "SELECT s FROM Student s";
        List<Student> arr = listFrom(hql,Student.class);

        ArrayList<clientUser> ss = new ArrayList<>();

        for( Student s: arr){
            ss.add(new clientUser(s.getName(),s.getId(),1));
        }

        return gson.toJson(ss);
    }

    String allRequests(int onlyActive){
        String hql = "SELECT r FROM Request r" + (onlyActive == 0 ? "" : " WHERE r.active = 1");
        List<Request> arr = listFrom(hql,Request.class);

        ArrayList<clientRequest> ss = new ArrayList<>();

        for( Request r: arr){
            clientRequest cr = new clientRequest(r.getTimeAdded(),r.getExplaination(),r.getId());
            Course c = r.getCourse();
            cr.course = new clientCourse(c.getName(),c.getId(),c.getOnline());
            Teacher t = c.getTeacher();
            cr.teacher = new clientUser(t.getName(),t.getId(),2);

            ss.add(cr);
        }

        return gson.toJson(ss);
    }

    void decideRequest(int requestID, boolean accept){
        Request r = App.session.get(Request.class,requestID);
        r.setActive(0);
        App.session.update(r);
        if(accept){
            Course c = r.getCourse();
            c.setDuration(c.getDuration() + r.getTimeAdded());
            App.session.update(c);
        }
        App.session.getTransaction().commit();
    }

    String coursesBySubject (int subjectID, int onlyConfirmed){
        String hql = "SELECT g.course , AVG(g.grade) FROM Grade g WHERE g.course.subject = " + subjectID + (onlyConfirmed == 0 ? "" : " AND g.confirmed = 1") + " GROUP BY g.course";
        List<Object[]> arr = listFrom(hql,Object[].class);

        ArrayList<clientCourse> cs = new ArrayList<>();
        for(Object[] o : arr){
            Course s = (Course)o[0];
            Double avg = (Double) o[1];
            cs.add(new clientCourse(s.getName(),s.getId(),s.getOnline(),avg));
        }
        return gson.toJson(cs);
    }

    String coursesByTeacher (String teacherID, int onlyConfirmed){
        String hql = "SELECT g.course , AVG(g.grade) FROM Grade g WHERE g.course.teacher = " + teacherID + (onlyConfirmed == 0 ? "" : " AND g.confirmed = 1") + " GROUP BY g.course";
        List<Object[]> arr = listFrom(hql,Object[].class);

        ArrayList<clientCourse> cs = new ArrayList<>();
        for(Object[] o : arr){
            Course s = (Course)o[0];
            Double avg = (Double) o[1];
            cs.add(new clientCourse(s.getName(),s.getId(),s.getOnline(),avg));
        }
        return gson.toJson(cs);
    }

    String examsByTeacher(String teacherID , int onlyConfirmed){
        String hql = "SELECT g.course , AVG(g.grade) FROM Grade g WHERE g.course.exam.teacher = " + teacherID + (onlyConfirmed == 0 ? "" : " AND g.confirmed = 1") + " GROUP BY g.course";
        List<Object[]> arr = listFrom(hql,Object[].class);

        ArrayList<clientCourse> cs = new ArrayList<>();
        for(Object[] o : arr){
            Course s = (Course)o[0];
            Double avg = (Double) o[1];
            cs.add(new clientCourse(s.getName(),s.getId(),s.getOnline(),avg));
        }
        return gson.toJson(cs);
    }

    <T> T getFirst (String hql, Class<T> obj){
        Query<T> query = App.session.createQuery(hql, obj);
        return query.getSingleResult();
    }

    <T> List<T> listFrom (String hql, Class<T> obj ){
        Query<T> query = App.session.createQuery(hql, obj);
        return query.list();
    }



}


