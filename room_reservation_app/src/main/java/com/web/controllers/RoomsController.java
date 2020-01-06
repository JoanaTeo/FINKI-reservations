package com.web.controllers;

import com.dao.UserRepository;
import com.models.*;
import com.models.exceptions.InvalidRoomNameException;
import com.services.ReservationsService;
import com.services.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rooms")
public class RoomsController {
    @Autowired
    private RoomsService roomsService;
    @Autowired
    private ReservationsService reservationsService;
    @Autowired
    private UserRepository usersRepository;

    @GetMapping("")
    public String listRooms(HttpServletRequest request) {
        createRoom("123",Building.TMF, 11);
        createRoom("223",Building.TMF, 11);
        createRoom("117",Building.MF, 11);
        createRoom("116",Building.MF, 11);
        createRoom("3.2",Building.B, 11);
        createRoom("3.1",Building.B, 11);
        createRoom("200AB",Building.LAB, 11);
        createRoom("200v",Building.LAB, 11);

        List<Room> rooms = this.roomsService.getAllRooms();
        Map<Building, List<Room>> buildingRooms = rooms.stream().collect(Collectors.groupingBy(a -> a.getBuilding()));
        request.setAttribute("rooms", rooms);
        request.setAttribute("buildingRooms", buildingRooms);
        request.setAttribute("bodyContent", "rooms");
        return "rooms-layout";
    }


    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public ModelAndView showCreateRoom() {
        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("bodyContent", "create-room");
        return modelAndView;
    }
    @PostMapping("/create")
    public String createRoom(@RequestParam String name,
                             @RequestParam Building building,
                             @RequestParam Integer seats) {
        this.roomsService.createRoom(name, seats, building);
        return "redirect:/rooms";
    }
    @PostMapping("/reserve/{name}")
    public String createReservation(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd")String date,
                                    @RequestParam Integer from,
                                    @RequestParam Integer to,
                                    @PathVariable String name,
                                    Principal principal) {
        User u = this.usersRepository.findByEmail(principal.getName());
        if(this.reservationsService.makeReservation(date, from, to, u, name ))
        {
            return "redirect:/rooms?success";
        }
        else
            return "redirect:/rooms?no";

    }

    @GetMapping("/{name}")
    public ModelAndView showEditRoom(@PathVariable String name) {
       Room room = this.roomsService.findByName(name).get(0);

        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("bodyContent", "edit-room");
        modelAndView.addObject("room", room);
        return modelAndView;
    }

    @PostMapping("/{oldName}")
    public String updateRoom(@PathVariable String oldName,
                             @RequestParam String newName) {
        this.roomsService.updateRoom(oldName, newName);
        return "redirect:/rooms";

    }

    @PostMapping("/{name}/delete")
    public String deleteRoom(@PathVariable String name) {
        this.roomsService.deleteRoom(name);
        return "redirect:/rooms";
    }
}
