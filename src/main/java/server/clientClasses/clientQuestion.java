package server.clientClasses;

public class clientQuestion {
    public String question;
    public String right;
    public String wrong1, wrong2, wrong3;
    public String teacher;
    public int studentAnswer;
    public int id;

    public clientQuestion(int id, String question, String right, String wrong1, String wrong2, String wrong3, String teacher) {
        this.id = id;
        this.question = question;
        this.right = right;
        this.wrong1 = wrong1;
        this.wrong2 = wrong2;
        this.wrong3 = wrong3;
        this.teacher = teacher;
    }

    public clientQuestion(int id, String question, String right, String wrong1, String wrong2, String wrong3) {
        this.id = id;
        this.question = question;
        this.right = right;
        this.wrong1 = wrong1;
        this.wrong2 = wrong2;
        this.wrong3 = wrong3;
    }
    public clientQuestion(String question, String right, String wrong1, String wrong2, String wrong3) {
        this.question = question;
        this.right = right;
        this.wrong1 = wrong1;
        this.wrong2 = wrong2;
        this.wrong3 = wrong3;
    }
}
