package com.services.impl;

import com.dao.ReservationsRepository;
import com.models.*;
import com.services.ReservationsService;
import com.services.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public boolean makeReservation(String date, Integer start, Integer end, User user, String room) {
        Room r= this.roomsService.findByName(room).get(0);
        if(this.reservationsRepository.findByDateAndRoomName(date,room ).stream().filter(reservation -> reservation.getStartDate().equals(start)).count()>0){
             return false;
        }
        else {
            this.reservationsRepository.save(new Reservation(start,date,end,r ,user));
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
    public List<Reservation> findReservationsByDateAndRoomName(String date, String room) {
        return this.reservationsRepository.findByDateAndRoomName(date,room);
    }

    @Override
    public List<Reservation> findReservationsByDate(String date) {
        return this.reservationsRepository.findByDate(date);
    }

    @Override
    public List<Reservation> findReservationsByRoom(Room room) {
        return null;
    }

    @Override
    public List<Reservation> findReservationsByBuilding(Building building) {
        return this.reservationsRepository.findReservationsByBuilding(building);
    }
}
