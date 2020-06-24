package server.clientClasses;

public class clientUser {
    public String name;
    public String id;
    public int role;
    // 0 - error
    // 1 - student
    // 2 - teacher
    // 3 - principle

    public clientUser() {
    }

    public clientUser(String name, String id) {
        this.name = name;
        this.id = id;
    }
    public clientUser(String name, String id, int role) {
        this.name = name;
        this.id = id;
        this.role = role;
    }
}
