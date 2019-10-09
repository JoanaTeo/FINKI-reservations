package com.dao;

import com.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationsRepository extends JpaRepository<Reservation,Integer> {
}
