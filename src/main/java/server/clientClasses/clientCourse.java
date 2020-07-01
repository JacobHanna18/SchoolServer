package server.clientClasses;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class clientCourse {
    public String name;
    public String teacher;
    public int id;
    public int online;
    public int examID;

    public ArrayList<clientUser> students = new ArrayList<>();

    public Double average;

    public clientCourse(String name, int id, int online) {
        this.name = name;
        this.id = id;
    }

    public clientCourse(String name, int id, int examID, String teacher) {
        this.name = name;
        this.id = id;
        this.examID = examID;
        this.teacher = teacher;
    }

    public clientCourse(String name, int id, int online, Double  average) {
        this.name = name;
        this.id = id;
        this.online = online;
        this.average = average;
    }
}
