package server.entities;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Subject_id")
    Subject subject;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Teacher_id")
    Teacher teacher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Exam_id")
    Exam exam;
    int AccessCode;
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            targetEntity = Student.class
    )
    @JoinTable(
            name = "Students_Courses",
            joinColumns = @JoinColumn(name = "Course_id"),
            inverseJoinColumns = @JoinColumn(name = "Student_id")
    )
    List<Student> students= new ArrayList<Student>();;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    List<Grade> grades= new ArrayList<Grade>();;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    List<Request> requests= new ArrayList<Request>();;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    List<Answer> answers= new ArrayList<Answer>();;

    public Course(Subject subject, Teacher teacher) {
        setSubject(subject);
        setTeacher(teacher);
    }

    public Course() {
    }

    public int getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Exam getExam() {
        return exam;
    }

    public int getAccessCode() {
        return AccessCode;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addRequest (Request r){
        r.setCourse(this);
    }

    public void addAnswer(Answer s){
        s.setCourse(this);
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        subject.getCourses().add(this);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        teacher.getCourses().add(this);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        exam.getCourses().add(this);
    }

    public void setAccessCode(int accessCode) {
        AccessCode = accessCode;
    }

    public void addGrade (Grade g){
        g.setCourse(this);
    }

    public void addStudent (Student s){
        students.add(s);
        s.getCourses().add(this);
    }
}
