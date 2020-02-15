package com.web.controllers;

import com.dao.UserRepository;
import com.google.api.client.util.DateTime;
import com.models.*;
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
    @Autowired
    Code code;

    @GetMapping("/user")
    public ModelAndView showReservationsByUser(Principal principal) {
        User user = this.usersRepository.findByEmail(principal.getName());
        List<Reservation> reservations = this.reservationsService.findReservationsByUser(user.getUsername());
        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("bodyContent", "reservations-user");
        modelAndView.addObject("userName", principal.getName());
        modelAndView.addObject("reservations", reservations);
        return modelAndView;
    }
    @GetMapping("/building")
    public ModelAndView showReservationsByBuilding(@RequestParam String building, Principal principal) {
        List<Room> rooms = this.roomsService.getRoomsByBuilding(Building.valueOf(building));
        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("bodyContent", "reservations-building");
        modelAndView.addObject("rooms", rooms);
        modelAndView.addObject("userName", principal.getName());

        modelAndView.addObject("building", building);
        return modelAndView;
    }
    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable String id) throws Exception {
        Reservation r= this.reservationsService.findReservationById(id).orElseThrow(Exception::new);
        this.code.client.events().delete(r.getRoom().getCalendarId(), r.getEventId()).execute();
        this.reservationsService.deleteReservation(id);
        return "redirect:/reservations/user";
    }
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public ModelAndView showReservationsByDateAndRoom(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd")String date,
                                                      @RequestParam String roomName, Principal principal) throws ParseException {

        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("roomNames", this.roomsService.findAllRooms());
        modelAndView.addObject("userName", principal.getName());

        modelAndView.addObject("bodyContent", "reservations-all");



        if(roomName=="" && date!="") {
            DateTime dateTime = new DateTime(date.replace("/","-")+"T00:00:00-08:00");
            List<Reservation> reservations = this.reservationsService.findReservationsByDate(dateTime);
            Map<String, List<Reservation>> roomsReservation = reservations.stream().collect(Collectors.groupingBy(a -> a.getRoom().getName()));
            modelAndView.addObject("roomReservations", roomsReservation);
        }
        else if(roomName!="" && date!=""){

            List<Reservation> rrr = this.reservationsService.findAllReservations();
            List<Reservation> reservations = this.reservationsService.findReservationsByDateAndRoom(date.replace("/","-"),roomName);
            Map<String, List<Reservation>> roomsReservation = reservations.stream().collect(Collectors.groupingBy(a -> a.getRoom().getName()));
            modelAndView.addObject("roomReservations", roomsReservation);
        }
        return modelAndView;

    }
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ModelAndView showDatePickerForReservation(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("roomNames", this.roomsService.findAllRooms());
        modelAndView.addObject("bodyContent", "reservations-all");
        modelAndView.addObject("userName", principal.getName());

        return modelAndView;
    }
}
