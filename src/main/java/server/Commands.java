package server;

import com.google.gson.Gson;
import server.clientClasses.clientGrade;
import server.entities.*;

import javax.persistence.criteria.*;
import org.hibernate.*;
import org.hibernate.query.Query;

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
            if(s.getPass() == password){
                currentUser = user;
                userType = Global.student;
                js.put(Global.success,true);
                js.put(Global.userType,Global.student);
                return js.toString();
            }
        }
        Teacher t = App.session.get(Teacher.class,user);
        if(t != null){
            if(t.getPass() == password){
                currentUser = user;
                userType = Global.teacher;
                js.put(Global.success,true);
                js.put(Global.userType,Global.teacher);
                return js.toString();
            }
        }
        Principle p = App.session.get(Principle.class,user);
        if(p != null){
            if(p.getPass() == password){
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

    String getGrades (String id){

        String hql = "FROM Grade g WHERE g.student = " + id;
        Query<Grade> query = App.session.createQuery(hql, Grade.class);
        List<Grade> l = query.list();
        clientGrade[] gs = new clientGrade[l.size()];
        for (int i = 0 ; i < l.size() ; i++){
            Grade g = l.get(i);
            gs[i] = new clientGrade(g.getGrade(),g.getId(),g.getCourse().getSubject().getName());
        }
        return (new Gson()).toJson(gs);

    }

    <T> String  toJson (List <T> l, T[] arr){
        for (int i = 0 ; i < l.size() ; i++){
            arr[i] = l.get(i);
        }
        return (new Gson()).toJson(arr);
    }

}


