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
    List<Student> students;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    List<Grade> grades;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    List<Request> requests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    List<Answer> answers;

    public Course(Subject subject, Teacher teacher) {
        this.subject = subject;
        this.teacher = teacher;
        this.students = null;
        this.grades = null;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public int getAccessCode() {
        return AccessCode;
    }

    public void setAccessCode(int accessCode) {
        AccessCode = accessCode;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void AddStudent(Student student) {
        students.add(student);
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void AddGrade(Grade grade) {
        grades.add(grade);
    }
}
