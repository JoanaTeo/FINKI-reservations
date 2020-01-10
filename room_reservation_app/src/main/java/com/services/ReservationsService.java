package com.services;

import com.google.api.client.util.DateTime;
import com.models.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public interface ReservationsService {
    boolean makeReservation(DateTime startDate, DateTime endDate, ReservationDescription description, User user, String room,String eventId);
    void deleteReservation(String id);
    List<Reservation> findAllReservations();
    List<Reservation> findReservationsByUser(String userId);
    Optional<Reservation> findReservationById(String id);
    List<Reservation> findReservationsByDateAndRoomName(DateTime date, String room);
    List<Reservation> findReservationsByDate(DateTime date);
    List<Reservation> findReservationsByRoom( Room room);
    List<Reservation> findReservationsByBuilding(Building building);

    List<Reservation> findReservationsByDateAndRoom(String replace, String roomName);
}
