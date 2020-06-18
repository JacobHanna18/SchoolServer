package server;

import com.google.gson.Gson;
import org.hibernate.query.Query;
import server.clientClasses.clientExam;
import server.clientClasses.clientQuestion;
import server.clientClasses.clientSubject;
import server.entities.*;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Commands {
    String currentUser;
    int userType;
    String LogIn (String user, String password){
        JSONObject js = new JSONObject();
        Student s = App.session.get(Student.class,user);
        if(s != null){
            if(s.getPass().equals(password)){
                currentUser = user;
                userType = Global.student;
                js.put(Global.success,true);
                js.put(Global.userType,Global.student);
                return js.toString();
            }
        }
        Teacher t = App.session.get(Teacher.class,user);
        if(t != null){
            if(t.getPass().equals(password)){
                currentUser = user;
                userType = Global.teacher;
                js.put(Global.success,true);
                js.put(Global.userType,Global.teacher);
                return js.toString();
            }
        }
        Principle p = App.session.get(Principle.class,user);
        if(p != null){
            if(p.getPass().equals(password)){
                currentUser = user;
                userType = Global.principle;
                js.put(Global.success,true);
                js.put(Global.userType,Global.principle);
                return js.toString();
            }
        }
        js.put(Global.success,false);
        return js.toString();
    }

    String teacherSubjectList (String teacherId){
        String hql = "SELECT c.subject FROM Course c WHERE c.teacher = " + teacherId;
        List<Subject> l = listFrom(hql,Subject.class);
        ArrayList<clientSubject> arr = new ArrayList<>();
        for (Subject s : l){
            arr.add(new clientSubject(s.getName(),s.getId()));
        }
        return (new Gson()).toJson(arr);
    }

    String getExam (int examID){
        Exam e = App.session.get(Exam.class, examID);

        clientExam exam = new clientExam(e.getTeacher().getName());
        for (Question q : e.getQuestions()){
            exam.questions.add(new clientQuestion(q.getQ(),q.getRightAnswer(),q.getWrongAnswer1(),q.getWrongAnswer2(),q.getWrongAnswer3(),q.getTeacher().getName()));
        }
        return (new Gson()).toJson(exam);
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
//        return (new Gson()).toJson(gs);
//
//    }


}


