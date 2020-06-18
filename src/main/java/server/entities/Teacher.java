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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teacher")
    List<Question> Questions = new ArrayList<Question>();;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teacher")
    List<Exam> Exams = new ArrayList<Exam>();;

    public List<Question> getQuestions() {
        return Questions;
    }

    public void addQuestion(Question q) {
        q.setTeacher(this);
    }

    public List<Exam> getExams() {
        return Exams;
    }

    public void addExam(Exam e) {
        e.setTeacher(this);
    }



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
