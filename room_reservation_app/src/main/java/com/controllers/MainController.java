package com.controllers;

import com.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dao.RoomsRepository;

@RestController
@RequestMapping("/user")
public class MainController {
    @Autowired
    private RoomsRepository roomsRepository;

    @PostMapping("/addRoom")
    public String addRoom(@RequestParam String id, @RequestParam String seats){
        Room newRoom = new Room();
        newRoom.setId(Integer.valueOf(id));
        newRoom.setSeats(Integer.valueOf(seats));
        roomsRepository.save(newRoom);
        return "Saved";
    }

    @GetMapping("/all")
    public Iterable<Room> getAllRooms() {
        return roomsRepository.findAll();
    }
}
