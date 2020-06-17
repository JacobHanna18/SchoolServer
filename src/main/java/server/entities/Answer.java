package server.entities;

import javax.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    int answer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Student_id")
    Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Question_id")
    Question question;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Course_id")
    Course course;

    public Answer() {
    }


    public int getId() {
        return id;
    }

    public int getAnswer() {
        return answer;
    }

    public Student getStudent() {
        return student;
    }

    public Question getQuestion() {
        return question;
    }

    public Course getCourse() {
        return course;
    }

    public void setAnswer(int answer) {
        this.answer = answer;

    }

    public void setStudent(Student student) {
        this.student = student;
        student.getAnswers().add(this);
    }

    public void setQuestion(Question question) {
        this.question = question;
        question.getAnswers().add(this);
    }

    public void setCourse(Course course) {
        this.course = course;
        course.getAnswers().add(this);
    }
}
