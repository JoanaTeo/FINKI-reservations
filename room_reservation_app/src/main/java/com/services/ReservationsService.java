package com.services;

import com.models.Building;
import com.models.Reservation;
import com.models.Room;
import com.models.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public interface ReservationsService {
    boolean makeReservation(String date, Integer start, Integer end, User user, String room);
    void deleteReservation(String id);
    List<Reservation> findAllReservations();
    List<Reservation> findReservationsByUser(String userId);
    Optional<Reservation> findReservationById(String id);
    List<Reservation> findReservationsByDateAndRoomName(String date, String room);
    List<Reservation> findReservationsByDate(String date);
    List<Reservation> findReservationsByRoom( Room room);
    List<Reservation> findReservationsByBuilding(Building building);
}
