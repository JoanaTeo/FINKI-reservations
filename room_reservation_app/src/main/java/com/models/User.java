package com.models;


import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    String id;
    String name;
    @OneToMany(mappedBy = "creator")
    List<Room> rooms;
    @OneToMany(mappedBy = "user")
    List<Reservation> reservations;
    public User(String name){
        this.name=name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
