package server.entities;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name = "teachers")
public class Teacher extends  User{
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teacher")
    List<Course> Courses = new ArrayList<Course>();;
    public Teacher(String name, String id, String pass){
        super(name, id, pass);

    }

    public Teacher() {
    }

    public List<Course> getCourses() {
        return Courses;
    }

    public void addCourse (Course c){
        c.setTeacher(this);
    }
}
