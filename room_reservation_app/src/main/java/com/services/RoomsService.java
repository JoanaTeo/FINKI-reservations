package com.services;

import com.models.Building;
import com.models.Room;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RoomsService {
    Room createRoom(String name, Integer seats, Building building, String calendarId);
    void deleteRoom(String id);
    void updateRoom(String name, String newName);
    List<Room> getAllRooms();
    List<Room> getRoomsByBuilding(Building building);
    List<Room> findByName(String name);
    Optional<Room> findById(String name);

    List<Room> findAllRooms();
}
