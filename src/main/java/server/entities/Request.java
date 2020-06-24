package server.entities;

import javax.persistence.*;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    int timeAdded;
    String explaination;
    int active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Course_id")
    Course course;

    public Request() {
    }

    public int getId() {
        return id;
    }

    public int getTimeAdded() {
        return timeAdded;
    }

    public String getExplaination() {
        return explaination;
    }

    public Course getCourse() {
        return course;
    }

    public void setTimeAdded(int timeAdded) {
        this.timeAdded = timeAdded;
    }

    public void setExplaination(String explaination) {
        this.explaination = explaination;
    }

    public void setCourse(Course course) {
        this.course = course;
        course.getRequests().add(this);
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
