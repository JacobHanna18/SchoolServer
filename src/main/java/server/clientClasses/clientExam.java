package server.clientClasses;

import java.util.ArrayList;

public class clientExam {
    public ArrayList<clientQuestion> questions = new ArrayList<>();
    public String teacher;
    public int id;
    public int courseID;
    public ArrayList<Integer> questionIds = new ArrayList<Integer>();

    public String subjectName;

    public byte[] file;

    public int online;

    public clientExam(int id, String teacher, String subjectName) {
        this.id = id;
        this.teacher = teacher;
        this.subjectName = subjectName;
    }

    public clientExam() {
    }

}
