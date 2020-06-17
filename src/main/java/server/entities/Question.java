package server.entities;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String Q;
    String  RightAnswer;
    String WrongAnswer1;
    String WrongAnswer2;
    String WrongAnswer3;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Subject_id")
    Subject subject;
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            targetEntity = Exam.class
    )
    @JoinTable(
            name = "Questions_Exams",
            joinColumns = @JoinColumn(name = "Question_id"),
            inverseJoinColumns = @JoinColumn(name = "Exam_id")
    )
    List<Exam> exams;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    List<Answer> answers;

    public Question(String q, String rightAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        this.Q = q;
        this.RightAnswer = rightAnswer;
        this.WrongAnswer1 = wrongAnswer1;
        this.WrongAnswer2 = wrongAnswer2;
        this.WrongAnswer3 = wrongAnswer3;
        this.exams = null;
    }

    public Question() {
    }

    public String getQ() {
        return Q;
    }

    public void setQ(String q) {
        Q = q;
    }

    public String getRightAnswer() {
        return RightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        RightAnswer = rightAnswer;
    }

    public String getWrongAnswer1() {
        return WrongAnswer1;
    }

    public void setWrongAnswer1(String wrongAnswer1) {
        WrongAnswer1 = wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return WrongAnswer2;
    }

    public void setWrongAnswer2(String wrongAnswer2) {
        WrongAnswer2 = wrongAnswer2;
    }

    public String getWrongAnswer3() {
        return WrongAnswer3;
    }

    public void setWrongAnswer3(String wrongAnswer3) {
        WrongAnswer3 = wrongAnswer3;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void AddExam(Exam exam) {
        exams.add(exam);
    }
}
