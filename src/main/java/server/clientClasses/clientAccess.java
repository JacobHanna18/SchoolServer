package server.clientClasses;

import java.util.ArrayList;

public class clientAccess {

    public Operation op;

    public String userID;
    public String password;

    public String teacherID;
    public String studentID;

    public int examID;
    public int courseID;
    public int subjectID;

    public clientExam e;
    public clientQuestion q;
    public ArrayList<clientAnswer> arr;

    public int onlyConfirmed;
    public int onlyActive;

    public int AccessCode;
    public int duration;
    public int online;

    public int addedTime;
    public String exp;

    public int newGrade;
    public String reason;

    public boolean accept;
}
