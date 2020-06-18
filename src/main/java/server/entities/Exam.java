package server.entities;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToMany(mappedBy = "exams",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            targetEntity = Question.class
    )
    List<Question> Questions = new ArrayList<Question>();;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Subject_id")
    Subject subject;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    List<Course> courses = new ArrayList<Course>();

    public Exam(Subject subject) {
        setSubject(subject);
    }

    public Exam() {
    }

    public int getId() {
        return id;
    }

    public List<Question> getQuestions() {
        return Questions;
    }

    public Subject getSubject() {
        return subject;
    }

    public List<Course> getCourses() {
        return courses;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    Teacher teacher;

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        teacher.getExams().add(this);
    }

    public void addCourse (Course c){
        c.setExam(this);
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        subject.getExams().add(this);
    }

    public void addQuestion (Question q){
        Questions.add(q);
        q.getExams().add(this);
    }

}
