package server.clientClasses;

import java.util.ArrayList;

public class clientExam {
    public ArrayList<clientQuestion> questions = new ArrayList<>();
    public String teacher;
    public int id;
    public int courseID;
    public ArrayList<Integer> questionIds = new ArrayList<Integer>();

    public String subjectName;
    public String note;
    public String teacherNote;

    public byte[] file;

    public int online;

    public int startTime;
    public int duration;
    public int accessCode;

    public clientExam(int id, String teacher, String subjectName, String note, String teacherNote) {
        this.id = id;
        this.teacher = teacher;
        this.subjectName = subjectName;
        this.note = note;
        this.teacherNote = teacherNote;
    }

    public clientExam() {
    }

}
