package server;

import server.entities.*;

import net.bytebuddy.agent.builder.AgentBuilder;
import org.hibernate.Criteria;
import org.json.simple.JSONObject;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

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

}


