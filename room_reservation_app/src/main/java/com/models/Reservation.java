package com.models;


import com.google.api.client.util.DateTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    String id;
    DateTime startDate;
    DateTime endDate;
    ReservationDescription description;
    String eventId;
    @ManyToOne
    Room room;
    @ManyToOne
    User user;

    public Reservation(DateTime startDate, DateTime endDate, Room room, User user, ReservationDescription description, String eventId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.room = room;
        this.user = user;
        this.description =description;
        this.eventId = eventId;
    }
    public Reservation(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReservationDescription getDescription() {
        return description;
    }

    public void setDescription(ReservationDescription description) {
        this.description = description;
    }

    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
