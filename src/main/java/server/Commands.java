package server;

import com.google.gson.Gson;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.hibernate.Session;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.query.Query;
import server.App;
import server.ServerClient.SimpleChatServer;
import server.clientClasses.*;
import server.entities.*;

import org.json.simple.JSONObject;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commands {

    public Session session;

    static Gson gson = new Gson();

    public clientUser LogIn (String userID, String password){
        JSONObject js = new JSONObject();
        clientUser cu = new clientUser();
        cu.id = userID;
        cu.role = 0;
        Student s = session.get(Student.class,userID);
        if(s != null){
            if(s.getPass().equals(password)){
                cu.name = s.getName();
                cu.role = 1;
            }
        }
        Teacher t = session.get(Teacher.class,userID);
        if(t != null){
            if(t.getPass().equals(password)){
                cu.name = t.getName();
                cu.role = 2;
            }
        }
        Principle p = session.get(Principle.class,userID);
        if(p != null){
            if(p.getPass().equals(password)){
                cu.name = p.getName();
                cu.role = 3;
            }
        }
        return cu;
    }

    public String teacherSubjectList (String teacherID){
        String hql = "SELECT c.subject FROM Course c WHERE c.teacher = " + teacherID;
        List<Subject> l = listFrom(hql,Subject.class);
        ArrayList<clientSubject> arr = new ArrayList<>();
        for (Subject s : l){
            arr.add(new clientSubject(s.getName(),s.getId()));
        }
        return gson.toJson(arr);
    }

    public String getExam (int examID){
        Exam e = session.get(Exam.class, examID);

        clientExam exam = new clientExam(e.getId(), e.getTeacher().getName(),e.getSubject().getName(),e.getNote(),e.getTeacherNote());
        for (Question q : e.getQuestions()){
            exam.questions.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return gson.toJson(exam);
    }

    public String examFromCourse (int courseID){
        Course c = session.get(Course.class, courseID);
        Exam e = c.getExam();

        if(e == null){
            clientExam exam = new clientExam(0, "",c.getSubject().getName(),"","");
            exam.accessCode = 0;
            return gson.toJson(exam);

        }
        clientExam exam = new clientExam(e.getId(), e.getTeacher().getName(),e.getSubject().getName(),e.getNote(),e.getTeacherNote());
        exam.online = c.getOnline();
        exam.accessCode = c.getAccessCode();
        for (Question q : e.getQuestions()){
            exam.questions.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return gson.toJson(exam);
    }

    public String selectExam (int examID, int courseID){
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        Course c = em.getReference(Course.class,courseID);
        Exam e = em.getReference(Exam.class,examID);
        c.setExam(e);


        em.persist(c);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return gson.toJson(new clientCompletion(true));
    }

    public String startExam (int courseID, int AccessCode, int duration, int online){
        String hql = "FROM Course c WHERE c.AccessCode = " + AccessCode;
        List<Course> l = listFrom(hql,Course.class);
        if(l.size() != 0){
            return gson.toJson(new clientCompletion(false));
        }
        Course c1 = session.get(Course.class,courseID);
        if(c1.getAccessCode() != 0){
            return gson.toJson(new clientCompletion(false));
        }
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        Course c = em.getReference(Course.class,courseID);

        c.setStartTime((int) (System.currentTimeMillis() / 1000));
        c.setAccessCode(AccessCode);
        c.setDuration(duration);
        c.setOnline(online);

        em.persist(c);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return gson.toJson(new clientCompletion(true));
    }

    public String subjectQuestionList (int subjectID){
        String hql = "FROM Question q WHERE q.subject = " + subjectID;
        List<Question> l = listFrom(hql,Question.class);
        ArrayList<clientQuestion> arr = new ArrayList<>();
        for (Question q : l){
            arr.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return gson.toJson(arr);
    }

    public String subjectExamList (int subjectID){
        String hql = "FROM Exam e WHERE e.subject = " + subjectID;
        List<Exam> l = listFrom(hql,Exam.class);
        ArrayList<clientExam> arr = new ArrayList<>();
        for (Exam e : l){
            arr.add(new clientExam(e.getId(),e.getTeacher().getName(),e.getSubject().getName(),e.getNote(),e.getTeacherNote()));
        }
        return gson.toJson(arr);
    }

    public String subjectTeacherExamList (int subjectID, String teacherID){
        String hql = "FROM Exam e WHERE e.subject = " + subjectID + " AND e.teacher = " + teacherID;
        List<Exam> l = listFrom(hql,Exam.class);
        ArrayList<clientExam> arr = new ArrayList<>();
        for (Exam e : l){
            arr.add(new clientExam(e.getId(),e.getTeacher().getName(),e.getSubject().getName(),e.getNote(),e.getTeacherNote()));
        }
        return gson.toJson(arr);
    }

    public void createExam (clientExam e, int subjectID, String teacherID){
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Exam newExam = new Exam();

        Subject s = em.getReference(Subject.class,subjectID);
        Teacher t = em.getReference(Teacher.class,teacherID);
        newExam.setSubject(s);
        newExam.setTeacher(t);
        newExam.setNote(e.note);
        newExam.setTeacherNote(e.teacherNote);
        for(int id : e.questionIds){
            Question q = em.getReference(Question.class,id);
            newExam.addQuestion(q);
        }
        em.persist(newExam);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void createQuestion (clientQuestion q, int subjectID, String teacherID){
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Question newQ = new Question(q.question,q.right,q.wrong1,q.wrong2,q.wrong3);
        Subject s = em.getReference(Subject.class,subjectID);
        Teacher t = em.getReference(Teacher.class,teacherID);
        newQ.setSubject(s);
        newQ.setTeacher(t);

        em.persist(newQ);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public String courseAverage (int courseID, int onlyConfirmed){
        String hql = "SELECT avg(grade) FROM Grade g where g.course = " + courseID + (onlyConfirmed==1 ? " AND g.confirmed = 1" : "");
        Double avg = getFirst(hql, Double.class);
        return gson.toJson(avg);
    }

    public String studentAverage (String studentID, int onlyConfirmed){
        String hql = "SELECT avg(grade) FROM Grade g where g.student = " + studentID + (onlyConfirmed==1 ? " AND g.confirmed = 1" : "");
        Double avg = getFirst(hql, Double.class);
        return gson.toJson(avg);
    }

    public String studentGrade (int courseID, String studentID, int onlyConfirmed){
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
        cg.courseName = g.getCourse().getName();
        cg.studentname = g.getStudent().getName();

        for (Answer a : l){
            clientAnswer ca = new clientAnswer(a.getAnswer(),a.getQuestion().getId(), a.getCourse().getId());
            Question q = a.getQuestion();
            ca.question = new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName());
            cg.answers.add(ca);

        }
        return gson.toJson(cg);
    }

    public String questionsOfTeacher (String teacherID){
        String hql = "FROM Question q WHERE q.teacher = " + teacherID ;
        List<Question> l = listFrom(hql,Question.class);
        ArrayList<clientQuestion> arr = new ArrayList<>();
        for (Question q : l){
            arr.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return gson.toJson(arr);
    }

    public String courseStudents (int courseID){
        String hql = "SELECT g.student FROM Grade g WHERE g.course = " + courseID;
        List<Student> l = listFrom(hql,Student.class);
        Course dbc = session.get(Course.class, courseID);
        clientCourse e = new clientCourse(dbc.getName(),dbc.getId(),dbc.getOnline());
        for (Student s : l){
            e.students.add(new clientUser(s.getName(),s.getId()));
        }
        return gson.toJson(e);
    }

    public String examsFromTeacher (String teacherID){
        String hql = "FROM Exam e WHERE e.teacher = " + teacherID ;
        List<Exam> l = listFrom(hql,Exam.class);
        ArrayList<clientExam> arr = new ArrayList<>();
        for (Exam e : l){
            clientExam ce = new clientExam(e.getId(),(e.getTeacher().getName()),e.getSubject().getName(),e.getNote(),e.getTeacherNote());
            for(Question q : e.getQuestions()){
                ce.questionIds.add(q.getId());
            }
            arr.add(ce);
        }
        return gson.toJson(arr);
        }

    public String coursesOfSubjectTeacher (int subjectId, String teacherID){
        String hql = "FROM Course c WHERE c.subject = " + subjectId + " AND c.teacher = " + teacherID;
        List<Course> l = listFrom(hql,Course.class);
        ArrayList<clientCourse> arr = new ArrayList<>();
        for (Course c : l){
            arr.add(new clientCourse(c.getName(),c.getId(),c.getOnline()));
        }
        return gson.toJson(arr);
    }

    public String coursesOfSubject(int subjectId){
        String hql = "FROM Course c WHERE c.subject = " + subjectId ;
        List<Course> l = listFrom(hql,Course.class);
        ArrayList<clientCourse> arr = new ArrayList<>();
        for (Course c : l){
            arr.add(new clientCourse(c.getName(),c.getId(),c.getOnline()));
        }
        return gson.toJson(arr);
    }

    public void newRequest (int courseID, int addedTime, String exp){
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        Request r = new Request();
        r.setExplaination(exp);
        r.setTimeAdded(addedTime);
        r.setActive(1);

        Course c = em.getReference(Course.class,courseID);
        r.setCourse(c);

        em.persist(r);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void confirmGrade (String studentID, int courseID){
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        String hql = "FROM Grade g WHERE g.course = " + courseID + " AND g.student = " + studentID;

        Grade g = em.createQuery(hql,Grade.class).getSingleResult();

        g.setConfirmed(1);

        em.persist(g);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void changeAndConfirmGrade (String studentID, int courseID, int newGrade, String reason){

        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();

        String hql = "FROM Grade g WHERE g.course = " + courseID + " AND g.student = " + studentID;

        Grade g = em.createQuery(hql,Grade.class).getSingleResult();

        g.setChangeReason(reason);
        g.setGrade(newGrade);
        g.setConfirmed(1);

        em.persist(g);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }



    public String getGrade (String studentID, int courseID){
        String hql = "FROM Grade g WHERE g.course = " + courseID + " AND g.student = " + studentID;

        Grade g = getFirst(hql,Grade.class);
        clientGrade cg = new clientGrade();
        cg.grade = g.getGrade();
        cg.course = new clientCourse(g.getCourse().getName(), g.getCourse().getId(),g.getCourse().getOnline());
        cg.student = new clientUser(g.getStudent().getName(), g.getStudent().getId());
        cg.courseName = g.getCourse().getName();
        cg.studentname = g.getStudent().getName();
        return gson.toJson(cg);
    }

    public String getGradesOfCourse (int courseID){
        String hql = "FROM Grade g WHERE g.course = " + courseID;
        List<Grade> gs = listFrom(hql,Grade.class);

        ArrayList <clientGrade> cgs = new ArrayList<>();

        for(Grade g : gs){
            clientGrade cg = new clientGrade();
            cg.grade = g.getGrade();
            cg.course = new clientCourse(g.getCourse().getName(), g.getCourse().getId(),g.getCourse().getOnline());
            cg.student = new clientUser(g.getStudent().getName(), g.getStudent().getId());
            cg.courseName = g.getCourse().getName();
            cg.studentname = g.getStudent().getName();
            cgs.add(cg);
        }

        return gson.toJson(cgs);
    }
    public String getGradesOfTeacher (String teacherID){
        String hql = "FROM Grade g WHERE g.course.teacher = " + teacherID;
        List<Grade> gs = listFrom(hql,Grade.class);

        ArrayList <clientGrade> cgs = new ArrayList<>();

        for(Grade g : gs){
            clientGrade cg = new clientGrade();
            cg.grade = g.getGrade();
            cg.course = new clientCourse(g.getCourse().getName(), g.getCourse().getId(),g.getCourse().getOnline());
            cg.student = new clientUser(g.getStudent().getName(), g.getStudent().getId());
            cg.courseName = g.getCourse().getName();
            cg.studentname = g.getStudent().getName();
            cgs.add(cg);
        }

        return gson.toJson(cgs);
    }

    public String getGradesOfStudent (String studentID, int onlyConfirmed){
        String hql = "FROM Grade g WHERE g.student = " + studentID + (onlyConfirmed == 1 ? " AND g.confirmed = 1" : "");
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
            cg.courseName = g.getCourse().getName();
            cg.studentname = g.getStudent().getName();
            cgs.add(cg);
        }

        return gson.toJson(cgs);
    }

    public clientExam getExamFrom (int AccessCode){
        String hql = "FROM Course c WHERE c.AccessCode = " + AccessCode;
        Course c = getFirst(hql, Course.class);

        clientExam ce = new clientExam();

        ce.online = c.getOnline();
        ce.subjectName = c.getName();
        ce.note = c.getExam().getNote();
        ce.courseID = c.getId();
        ce.teacherNote = c.getExam().getTeacherNote();
        Exam e = c.getExam();
        for (Question q : e.getQuestions()){
            ce.questions.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }

        return ce;
    }

    public String getStudentAnswers (int courseID, String studentID){
        String hql = "SELECT a FROM Answer a WHERE a.course = " + courseID + " AND a.student = " + studentID;
        List<Answer> arr = listFrom(hql,Answer.class);

        ArrayList<clientQuestion> as = new ArrayList<>();
        for(Answer a : arr){
            Question q = a.getQuestion();
            clientQuestion cq = new clientQuestion(q.getId(),q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3());
            cq.studentAnswer = a.getAnswer();
            as.add(cq);
        }
        return gson.toJson(as);
    }

    public String takeExam (int AccessCode, String studentID){

        Grade g = addStudentToCourse(studentID,AccessCode);
        if(g == null){
            return "";
        }

        if(g.isConfirmed() != -1){
            return  "";
        }

        Course c = g.getCourse();

        clientExam ce = new clientExam();

        int currentTime = ((int) (System.currentTimeMillis() / 1000));

        if(currentTime - c.getStartTime() > c.getDuration()){
            ce.online = -1;
            return gson.toJson(ce);
        }

        if(c.getOnline() == 0){
            return this.getManualExam(studentID,AccessCode);
        }

        ce.online = c.getOnline();
        ce.courseID = c.getId();
        ce.startTime=c.getStartTime();
        ce.duration=c.getDuration();

        Exam e = c.getExam();
        ce.teacherNote = e.getTeacherNote();
        ce.note = e.getNote();
        for (Question q : e.getQuestions()){
            ce.questions.add(new clientQuestion(q.getId(), q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return gson.toJson(ce);
    }

    public Grade addStudentToCourse (String studentID, int AccessCode){
        String hql = "FROM Course c WHERE c.AccessCode = " + AccessCode;
        List<Course> cs = listFrom(hql, Course.class);

        if(cs.size() == 0){
            return null;
        }

        Course c = getFirst(hql, Course.class);

        String hql2 = "SELECT g FROM Grade g WHERE g.course = " + c.getId() + " AND g.student = " + studentID;
        List<Grade> l2 = listFrom(hql2,Grade.class);
        if(l2.size() == 0) {
            Student s = session.get(Student.class, studentID);
            Grade g = new Grade();
            g.setStudent(s);
            g.setCourse(c);
            g.setConfirmed(-1);
            g.setGrade(0);
            g.setExamFile(null);
            session.save(g);
            session.flush();
            session.getTransaction().commit();
            return g;
        }
        return l2.get(0);
    }

    public String submitOnlineExam (ArrayList<clientAnswer> arr, int courseID, String studentID){

        Course cc = session.get(Course.class,courseID);

        if(!cc.isActive()){
            return gson.toJson(new clientCompletion(false));
        }

        int rightAnswers = 0;
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Course c = session.get(Course.class, courseID);
        Student s = em.getReference(Student.class, studentID);

        Grade g = addStudentToCourse(studentID,c.getAccessCode());

        if(g.isConfirmed() !=-1){
            return gson.toJson(new clientCompletion(false));
        }

        for (clientAnswer ca : arr){
            rightAnswers += (ca.answer == 1 ? 1 : 0);
            Answer newA = new Answer();
            newA.setAnswer(ca.answer);
            Question q = em.getReference(Question.class,ca.questionId);
            newA.setStudent(s);
            newA.setCourse(c);
            newA.setQuestion(q);
            em.persist(newA);
        }

        em.flush();
        em.getTransaction().commit();
        em.close();
        g.setGrade(100 * (double)rightAnswers / (double)g.getCourse().getExam().getQuestions().size());
        g.setConfirmed(0);
        session.persist(g);
        session.flush();
        session.getTransaction().commit();
        return gson.toJson(new clientCompletion(true));
    }

    public String allTeachers (){
        String hql = "SELECT t FROM Teacher t";
        List<Teacher> arr = listFrom(hql,Teacher.class);

        ArrayList<clientUser> ts = new ArrayList<>();

        for( Teacher t : arr){
            ts.add(new clientUser(t.getName(),t.getId(),2));
        }

        return gson.toJson(ts);
    }

    public String allSubjects(){
        String hql = "SELECT s FROM Subject s";
        List<Subject> arr = listFrom(hql,Subject.class);

        ArrayList<clientSubject> ss = new ArrayList<>();

        for( Subject s: arr){
            ss.add(new clientSubject(s.getName(),s.getId()));
        }

        return gson.toJson(ss);
    }

    public String allStudents (){
        String hql = "SELECT s FROM Student s";
        List<Student> arr = listFrom(hql,Student.class);

        ArrayList<clientUser> ss = new ArrayList<>();

        for( Student s: arr){
            ss.add(new clientUser(s.getName(),s.getId(),1));
        }

        return gson.toJson(ss);
    }

    public String allRequests(int onlyActive){
        String hql = "SELECT r FROM Request r" + (onlyActive == 0 ? "" : " WHERE r.active = 1");
        List<Request> arr = listFrom(hql,Request.class);

        ArrayList<clientRequest> ss = new ArrayList<>();

        for( Request r: arr){
            clientRequest cr = new clientRequest(r.getTimeAdded(),r.getExplaination(),r.getId());
            cr.course = r.getCourse().getName();
            cr.teacher = r.getCourse().getTeacher().getName();
            cr.timeAdded = r.getTimeAdded();

            ss.add(cr);
        }

        return gson.toJson(ss);
    }

    public void decideRequest(int requestID, boolean accept){
        Request r = session.get(Request.class,requestID);
        r.setActive(0);
        session.update(r);
        if(accept){
            Course c = r.getCourse();
            c.setDuration(c.getDuration() + r.getTimeAdded());
            session.update(c);
            clientAccess ca = new clientAccess();
            ca.addedTime = r.getTimeAdded();
            ca.courseID = r.getCourse().getId();
            ca.op = Operation.newRequest;
            SimpleChatServer.server.sendToAllClients(gson.toJson(ca));
        }
        session.flush();
        session.getTransaction().commit();
    }

    public String coursesBySubject (int subjectID, int onlyConfirmed){
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

    public String coursesFromTeacherExams (int subjectID, String teacherID){
        String hql = "SELECT c FROM Course c WHERE c.exam.teacher = " + teacherID + " AND c.subject = " + subjectID;
        List<Course> arr = listFrom(hql,Course.class);
        ArrayList<clientCourse> cs = new ArrayList<>();
        for(Course c : arr){
            cs.add(new clientCourse(c.getName(),c.getId(),c.getExam().getId(),c.getTeacher().getName()));
        }
        return gson.toJson(cs);
    }

    public String isCourseActive (int courseID){
        String hql = "FROM Course c WHERE c.id = " + courseID;
        Course c = getFirst(hql,Course.class);

        int endTime = c.getStartTime() + c.getDuration();
        int currentTime = ((int) (System.currentTimeMillis() / 1000));
        return gson.toJson(c.getStartTime() <= currentTime && endTime >= currentTime);
    }

    public String coursesByTeacher (String teacherID, int onlyConfirmed){
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

    public String examsByTeacher(String teacherID , int onlyConfirmed){
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

    public String submitManualExam (byte[] file, int courseID, String studentID){
        Course c = session.get(Course.class,courseID);

        if(!c.isActive()){
            return gson.toJson(new clientCompletion(false));
        }

        Grade g = addStudentToCourse(studentID,c.getAccessCode());

        if(g.isConfirmed() !=-1){
            return gson.toJson(new clientCompletion(false));
        }

        g.setExamFile(file);
        g.setConfirmed(0);
        session.update(g);
        session.flush();
        session.getTransaction().commit();
        return gson.toJson(new clientCompletion(true));
    }

    public String downloadStudentExam(String studentID, int courseID){
        String hql = "SELECT g FROM Grade g WHERE g.student = " + studentID + " AND g.course = " + courseID;
        Grade g = getFirst(hql, Grade.class);
        clientGrade cg = new clientGrade();
        cg.courseName = g.getCourse().getName();
        cg.file = g.getExamFile();
        cg.grade = g.getGrade();
        cg.studentname = g.getStudent().getName();
        return gson.toJson(cg);
    }

    public String getManualExam(String studentID, int AccessCode) {
        Grade g = addStudentToCourse(studentID,AccessCode);

        Exam e = g.getCourse().getExam();

        XWPFDocument document = new XWPFDocument();

        XWPFParagraph p1 = document.createParagraph();
        p1.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r1 = p1.createRun();
        r1.setBold(true);
        r1.setFontSize(24);
        r1.setText(g.getCourse().getName());

        int x = 1;
        for(Question q : e.getQuestions()){

            XWPFParagraph paragraph = document.createParagraph();

            ArrayList<String> arr = new ArrayList<>();
            arr.add(q.getRightAnswer());
            arr.add(q.getWrongAnswer1());
            arr.add(q.getWrongAnswer2());
            arr.add(q.getWrongAnswer3());
            Collections.shuffle(arr);

            XWPFRun questionRun = paragraph.createRun();
            questionRun.setBold(true);
            questionRun.setText(x + ". " + q.getQ());
            x++;
            questionRun.addBreak();
            XWPFRun answersRun = paragraph.createRun();

            int y = 1;
            for (String s : arr){
                answersRun.addTab();
                answersRun.setText(y + ". " + s);
                y++;
                answersRun.addBreak();
            }

        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            document.write(out);
            out.close();
            document.close();
        } catch (IOException err) {
            err.printStackTrace();
        }


        clientExam manual = new clientExam();
        manual.file = out.toByteArray();
        manual.courseID = g.getCourse().getId();
        manual.online = 0;
        manual.note = e.getNote();
        manual.teacherNote = e.getTeacherNote();
        manual.duration = g.getCourse().getDuration();
        manual.startTime = g.getCourse().getStartTime();

        return gson.toJson(manual);
    }


    <T> T getFirst (String hql, Class<T> obj){
        Query<T> query = session.createQuery(hql, obj);
        return query.getSingleResult();
    }

    <T> List<T> listFrom (String hql, Class<T> obj ){
        Query<T> query = session.createQuery(hql, obj);
        return query.list();
    }

}


