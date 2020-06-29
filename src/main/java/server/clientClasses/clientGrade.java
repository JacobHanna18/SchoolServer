package server.clientClasses;

import java.util.ArrayList;

public class clientGrade {
    public clientUser student;
    public double grade;
    public clientCourse course;
    public String courseName;
    public int online;
    public ArrayList<clientAnswer> answers = new ArrayList<>();
}
