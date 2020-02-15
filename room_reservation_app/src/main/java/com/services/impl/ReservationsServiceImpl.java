package com.services.impl;

import com.dao.ReservationsRepository;
import com.google.api.client.util.DateTime;
import com.models.*;
import com.services.ReservationsService;
import com.services.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationsServiceImpl implements ReservationsService {
    @Autowired
    ReservationsRepository reservationsRepository;
    @Autowired
    RoomsService roomsService;

    @Override
    @Transactional
    public boolean makeReservation(DateTime startDate, DateTime endDate, ReservationDescription description, User user, String room,String eventId) {
        Room r= this.roomsService.findByName(room).get(0);
        if(!this.reservationsRepository.findByStartDateAndRoomName(startDate,room).isEmpty()){
             return false;
        }else {
            this.reservationsRepository.save(new Reservation(startDate,endDate,r ,user,description,eventId));
            return true;
        }
    }

    @Override
    @Transactional
    public void deleteReservation(String id) {
        this.reservationsRepository.deleteById(id);
    }


    @Override
    public List<Reservation> findAllReservations() {
        return this.reservationsRepository.findAll();
    }

    @Override
    public List<Reservation> findReservationsByUser(String userId) {
        return this.reservationsRepository.findAllByUserUsername(userId);
    }

    @Override
    public Optional<Reservation> findReservationById(String id) {
        return this.reservationsRepository.findById(id);
    }

    @Override
    public List<Reservation> findReservationsByDateAndRoomName(DateTime date, String room) {
        return this.reservationsRepository.findByStartDateAndRoomName(date,room);
    }

    @Override
    public List<Reservation> findReservationsByDate(DateTime date) {
        return this.reservationsRepository.findByStartDate(date);
    }

    @Override
    public List<Reservation> findReservationsByRoom(Room room) {
        return null;
    }

    @Override
    public List<Reservation> findReservationsByBuilding(Building building) {
        return this.reservationsRepository.findReservationsByBuilding(building);
    }

    @Override
    public List<Reservation> findReservationsByDateAndRoom(String date, String roomName) {
        List dates= new ArrayList();
        dates.add(new DateTime(date.replace("/","-")+"T08:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T09:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T10:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T11:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T12:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T13:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T14:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T15:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T16:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T17:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T18:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T19:00:00-08:00"));
        dates.add(new DateTime(date.replace("/","-")+"T20:00:00-08:00"));

        return this.reservationsRepository.findReservationsByDateAndRoom(dates,roomName);
    }
}
