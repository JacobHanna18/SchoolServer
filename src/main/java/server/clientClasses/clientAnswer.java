package server.clientClasses;

public class clientAnswer {
    public int id;
    public int answerId;
    public int questionId;
    public int courseId;

    public clientAnswer(int id, int answerId, int questionId, int courseId) {
        this.id = id;
        this.answerId = answerId;
        this.questionId = questionId;
        this.courseId = courseId;
    }
}
