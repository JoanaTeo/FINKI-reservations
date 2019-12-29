package com.web.controllers;

import com.dao.UserRepository;
import com.models.Building;
import com.models.Reservation;
import com.models.Room;
import com.models.User;
import com.services.ReservationsService;
import com.services.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservations")
public class ReservationsController {
    @Autowired
    ReservationsService reservationsService;
    @Autowired
    UserRepository usersRepository;
    @Autowired
    RoomsService roomsService;

    @GetMapping("/user")
    public ModelAndView showReservationsByUser(Principal principal) {
        User user = this.usersRepository.findByEmail(principal.getName());
        List<Reservation> reservations = this.reservationsService.findReservationsByUser(user.getUsername());
        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("bodyContent", "reservations-user");
        modelAndView.addObject("reservations", reservations);
        return modelAndView;
    }
//    @GetMapping("/{room}")
//    public ModelAndView showReservationsForRoom(@PathVariable String room) {
//        List<Reservation>
//        ModelAndView modelAndView = new ModelAndView("rooms-layout");
//        modelAndView.addObject("bodyContent", "reservations-room");
//        modelAndView.addObject("room", room);
//        modelAndView.addObject("reservations", reservations);
//        return modelAndView;
//    }
//    @PostMapping("/{room}")
//    public ModelAndView showReservationsForRoomPost(@PathVariable String room, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd")String date) {
//        List<Reservation> reservations = this.reservationsService.findReservationsByDateAndRoomName(date, room);
//        ModelAndView modelAndView = new ModelAndView("rooms-layout");
//        modelAndView.addObject("bodyContent", "reservations-room");
//        modelAndView.addObject("room", room);
//        modelAndView.addObject("reservations", reservations);
//        return modelAndView;
//    }
    @GetMapping("/building")
    public ModelAndView showReservationsByBuilding(@RequestParam String building) {
        List<Room> room = this.roomsService.getRoomsByBuilding(Building.valueOf(building));
//        Map<String, List<Reservation>> roomsReservation = reservations.stream().
////                collect(Collectors.groupingBy(a -> a.getRoom().getName()));
        List rooms = new ArrayList();
        rooms.add("dsada");
        rooms.add("dsada2");

        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("bodyContent", "reservations-building");
        modelAndView.addObject("rooms", rooms);
        return modelAndView;
    }
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public ModelAndView showReservationsByDateAndRoom(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd")String date,
                                                      @RequestParam String roomName) throws ParseException {

        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("roomNames", this.roomsService.findAllRooms());
        modelAndView.addObject("bodyContent", "reservations-all");
        if(roomName=="" && date!="") {
            List<Reservation> reservations = this.reservationsService.findReservationsByDate(date);
            Map<String, List<Reservation>> roomsReservation = reservations.stream().collect(Collectors.groupingBy(a -> a.getRoom().getName()));
            modelAndView.addObject("roomReservations", roomsReservation);
        }
        else if(roomName!="" && date!=""){
            List<Reservation> reservations = this.reservationsService.findReservationsByDateAndRoomName(date,roomName);
            Map<String, List<Reservation>> roomsReservation = reservations.stream().collect(Collectors.groupingBy(a -> a.getRoom().getName()));
            modelAndView.addObject("roomReservations", roomsReservation);
        }
        return modelAndView;

    }
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ModelAndView showDatePickerForReservation() {
        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("roomNames", this.roomsService.findAllRooms());
        modelAndView.addObject("bodyContent", "reservations-all");
        return modelAndView;
    }
}
