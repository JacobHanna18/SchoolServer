package server.clientClasses;

public class clientAnswer {
    public int id;
    public int answer;
    public int questionId;
    public int courseId;


    public clientAnswer(int id, int answer, int questionId, int courseId) {
        this.id = id;
        this.answer = answer;
        this.questionId = questionId;
        this.courseId = courseId;
    }
}
