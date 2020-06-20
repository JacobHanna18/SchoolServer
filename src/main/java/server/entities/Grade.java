package server.entities;

import javax.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    double grade;


    String changeReason;

    public int isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    int confirmed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Student_id")
    Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Course_id")
    Course course;

    public Grade() {
    }

    public Grade(int grade, Student student, Course course) {
        setGrade(grade);
        setStudent(student);
        setCourse(course);
    }

    public int getId() {
        return id;
    }

    public double getGrade() {
        return grade;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public void setStudent(Student student) {
        this.student = student;
        student.getGrades().add(this);
    }

    public void setCourse(Course course) {
        this.course = course;
        course.getGrades().add(this);
    }
}
