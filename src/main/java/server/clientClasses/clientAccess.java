package server.clientClasses;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
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
    public int requestID;

    public byte[] file;

     public String note;
    public String teacherNote;

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

    static public void toFile (byte[] bytes, String FilePath) throws IOException {
        OutputStream os = new FileOutputStream(FilePath);
        os.write(bytes);
        os.close();
    }

    static public byte[] toBytes (String FilePath) throws IOException {
        FileInputStream fis = null;
        File file = new File(FilePath);
        byte[] bArray = new byte[(int) file.length()];
        fis = new FileInputStream(file);
        fis.read(bArray);
        fis.close();
        return bArray;
    }
}
