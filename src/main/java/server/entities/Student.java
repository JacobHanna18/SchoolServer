package server.entities;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "students")
public class Student extends User {

    @ManyToMany(mappedBy = "students",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            targetEntity = Course.class
    )
    List<Course> Courses;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    List<Grade> Grades;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    List<Answer> answers;
    public Student(String name, String id, String pass){
        super(name, id, pass);
        this.Courses = null;
        this.Grades = null;
    }
}
