package server;

import com.google.gson.Gson;
import server.clientClasses.clientGrade;
import server.entities.*;

import net.bytebuddy.agent.builder.AgentBuilder;
import org.hibernate.Criteria;
import org.json.simple.JSONObject;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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

    String getGrades (String StudentID){
        Student s = App.session.get(Student.class,StudentID);
        System.out.println(1);
        List<Grade> grades = s.getGrades();
        System.out.println(100);
        System.out.println(grades);
        System.out.println(grades.size());
        System.out.println("70");
        clientGrade[] arr = new clientGrade[grades.size()];

        for (int i=0;i<grades.size();i++){
            System.out.println(3);
            Grade g = grades.get(i);
            arr[i] = new clientGrade(g.getGrade(),g.getStudent().getId(),g.getCourse().getId());
        }

        return (new Gson()).toJson(arr);
    }

}


