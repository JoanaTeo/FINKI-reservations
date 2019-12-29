package com.dao;

import com.models.Building;
import com.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoomsRepository extends JpaRepository<Room, String> {
    List<Room> findByName(String name);

    List<Room> findByBuilding(Building building);
}
