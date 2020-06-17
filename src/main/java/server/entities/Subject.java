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
        List<Question> questions;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
        List<Exam> exams;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
        List<Course> courses;

        public Subject(String name) {
                Name = name;
                courses = null;
                exams = null;
        }

        public Subject() {
        }

        public String getName() {
                return Name;
        }

        public void setName(String name) {
                Name = name;
        }

        public List<Question> getQuestions() {
                return questions;
        }

        public void AddQuestions(Question question) {
                questions.add(question);
        }

        public List<Exam> getExams() {
                return exams;
        }

        public void AddExam(Exam exam) {
                exams.add(exam);
        }

        public List<Course> getCourses() {
                return courses;
        }

        public void AddCourse(Course course) {
                courses.add(course);
        }
}
