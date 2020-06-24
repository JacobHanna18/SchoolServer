package server.clientClasses;

public class clientRequest {
    public clientCourse course;
    public clientUser teacher;
    public int timeAdded;
    public String explanation;
    public int id;

    public clientRequest(int timeAdded, String explanation, int id) {
        this.timeAdded = timeAdded;
        this.explanation = explanation;
        this.id = id;
    }
}
