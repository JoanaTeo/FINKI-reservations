package com.dao;

import com.google.api.client.util.DateTime;
import com.models.Building;
import com.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface ReservationsRepository extends JpaRepository<Reservation,String> {

    List<Reservation> findAllByUserUsername(String id);

    List<Reservation> findByStartDateAndRoomName(DateTime date, String roomName);
    @Query("select c from Reservation c " +
            "WHERE c.room.building like :building")
    List<Reservation> findReservationsByBuilding(@Param("building") Building building);
    @Query("select c from Reservation c " +
            "WHERE c.room.name like :roomName and c.startDate in :date")
    List<Reservation> findReservationsByDateAndRoom(@Param("date")List date, @Param("roomName")String roomName);
    List<Reservation> findByStartDate(DateTime date);
}
