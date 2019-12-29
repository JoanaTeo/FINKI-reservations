package com.services.impl;

import com.dao.RoomsRepository;
import com.models.Building;
import com.models.Room;
import com.models.exceptions.InvalidRoomNameException;
import com.services.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomsServiceImpl implements RoomsService {


    @Autowired
    RoomsRepository roomsRepository;
    @Override
    public Room createRoom(String name, Integer seats, Building building) {
        Room room = new Room(name,seats, building );
        this.roomsRepository.save(room);
        return room;
    }

    @Override
    public void deleteRoom(String name) {
        this.roomsRepository.deleteById(name);
    }

    @Override
    public void updateRoom(String name, String newName) {
        Room room = this.roomsRepository.findById(name).orElseThrow(InvalidRoomNameException::new);
        room.setName(name);
        this.roomsRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
      return this.roomsRepository.findAll();
    }

    @Override
    public List<Room> getRoomsByBuilding(Building building) {
        return this.roomsRepository.findByBuilding(building);
    }

    @Override
    public List<Room> findByName(String name) {
       return this.roomsRepository.findByName(name);
    }
    @Override
    public Optional<Room> findById(String name) {
        return this.roomsRepository.findById(name);
    }

    @Override
    public List<Room> findAllRooms() {
        return this.roomsRepository.findAll();
    }

}
