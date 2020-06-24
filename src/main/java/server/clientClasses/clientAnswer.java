package server.clientClasses;

public class clientAnswer {
    public int answer;
    public int questionId;
    public int courseId;

    public clientQuestion question;

    public clientAnswer(int answer, int questionId, int courseId) {
        this.answer = answer;
        this.questionId = questionId;
        this.courseId = courseId;
    }
}
