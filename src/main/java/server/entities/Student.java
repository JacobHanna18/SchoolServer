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
    List<Course> Courses = new ArrayList<Course>();;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    List<Grade> Grades = new ArrayList<Grade>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    List<Answer> answers = new ArrayList<Answer>();;


    public Student(String name, String id, String pass){
        super(name, id, pass);
    }

    public Student() {
    }

    public List<Course> getCourses() {
        return Courses;
    }

    public List<Grade> getGrades() {
        return Grades;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer s){
        s.setStudent(this);
    }

    public void addGrade (Grade g){
        g.setStudent(this);
    }

    public void addCourse (Course c){
        c.addStudent(this);
    }
}
