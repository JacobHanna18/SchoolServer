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
    List<Question> Questions;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Subject_id")
    Subject subject;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    List<Course> courses;

    public Exam(Subject subject) {
        this.Questions = null;
        this.subject = subject;
    }

    public List<Question> getQuestions() {
        return Questions;
    }

    public void AddQuestions(Question question) {
        Questions.add(question);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
