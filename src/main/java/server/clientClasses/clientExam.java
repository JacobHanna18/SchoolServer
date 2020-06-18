package server.clientClasses;

import java.util.ArrayList;

public class clientExam {
    public ArrayList<clientQuestion> questions = new ArrayList<>();
    public String teacher;

    public clientExam(String teacher) {
        this.teacher = teacher;
    }

}
