package server.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "principles")
public class Principle extends User {

    public Principle(String name, String id, String pass){
        super(name, id, pass);
    }
}
