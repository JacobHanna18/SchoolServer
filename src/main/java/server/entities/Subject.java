package server.entities;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        String Name;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
        List<Question> questions = new ArrayList<Question>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
        List<Exam> exams = new ArrayList<Exam>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
        List<Course> courses = new ArrayList<Course>();

        public Subject(String name) {
                Name = name;
        }

        public Subject() {
        }


        public int getId() {
                return id;
        }

        public String getName() {
                return Name;
        }

        public List<Question> getQuestions() {
                return questions;
        }

        public List<Exam> getExams() {
                return exams;
        }

        public List<Course> getCourses() {
                return courses;
        }

        public void setName(String name) {
                Name = name;
        }

        public void addQuestion (Question q){
                q.setSubject(this);
        }

        public void addCourse (Course c){
                c.setSubject(this);
        }

        public void addExam(Exam e){
                e.setSubject(this);
        }
}
