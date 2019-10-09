package com.models;

import javax.jws.soap.SOAPBinding;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Room {
    @Id
    Integer id;
    Integer seats;
    @ManyToOne
    User creator;
    @OneToMany(mappedBy = "room")
    List<Reservation> reservations;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
