package server.clientClasses;

import java.util.ArrayList;

public class clientExam {
    public ArrayList<clientQuestion> questions = new ArrayList<>();
    public String teacher;
    public int id;
    public ArrayList<Integer> questionIds = new ArrayList<Integer>();

    public int online;

    public clientExam(int id, String teacher) {
        this.id = id;
        this.teacher = teacher;
    }

    public clientExam() {
    }

}
