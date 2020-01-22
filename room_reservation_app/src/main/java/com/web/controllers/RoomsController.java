package com.web.controllers;

import com.dao.UserRepository;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.models.*;
import com.models.exceptions.InvalidRoomNameException;
import com.services.ReservationsService;
import com.services.RoomsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
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
    @Autowired Code code;

    private final static Log logger = LogFactory.getLog(GoogleCalController.class);


    @GetMapping("")
    public String listRooms(HttpServletRequest request,Principal principal, @RequestParam(required = false) String code, HttpSession session) throws Exception {

        List<Room> rooms = this.roomsService.getAllRooms();
        if(code!=null) {
            this.code.authorize();
            this.code.credentials(code);
        }
        Map<Building, List<Room>> buildingRooms = rooms.stream().collect(Collectors.groupingBy(a -> a.getBuilding()));
        request.setAttribute("rooms", rooms);
        request.setAttribute("buildingRooms", buildingRooms);
        request.setAttribute("userName", principal.getName());
        request.setAttribute("bodyContent", "rooms");
        return "rooms-layout";
    }


    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public ModelAndView showCreateRoom(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("bodyContent", "create-room");
        modelAndView.addObject("userName", principal.getName());
        return modelAndView;
    }
    @PostMapping("/create")
    public String createRoom(@RequestParam String name,
                             @RequestParam Building building,
                             @RequestParam Integer seats,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) throws Exception {

            com.google.api.services.calendar.model.Calendar calendar1 = new com.google.api.services.calendar.model.Calendar();
            calendar1.setSummary(name);
            calendar1.setTimeZone("America/Los_Angeles");

            com.google.api.services.calendar.model.Calendar createdCalendar = this.code.client.calendars().insert(calendar1).execute();
        this.roomsService.createRoom(name, seats, building, createdCalendar.getId());
            System.out.println(createdCalendar.getId());
        return "redirect:/rooms";
    }
    @PostMapping("/reserve")
    public String createReservation(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd")String date,
                                    @RequestParam Integer from,
                                    @RequestParam String name,
                                    @RequestParam ReservationDescription description,
                                    Principal principal,
                                    HttpSession session) throws Exception {
        User u = this.usersRepository.findByEmail(principal.getName());

        DateTime startDateTime = new DateTime("2020-01-11T09:00:00-07:00");
        DateTime endDateTime = new DateTime("2020-01-11T09:00:00-07:00");
        if(from<10){
            startDateTime = new DateTime(date.replace("/","-")+"T0"+from+":00:00-08:00");
        }
        else {
             startDateTime = new DateTime(date.replace("/","-")+"T"+from+":00:00-08:00");
        }
        Integer to = from+1;
        if(from+1<10){

             endDateTime = new DateTime(date.replace("/","-")+"T0"+to+":00:00-08:00");
        }
        else {
             endDateTime = new DateTime(date.replace("/","-")+"T"+to+":00:00-08:00");
        }

        String calendarId = this.roomsService.findByName(name).get(0).getCalendarId();

            try {
                com.google.api.services.calendar.Calendar.Events events = this.code.client.events();
                Event event = new Event()
                    .setSummary(description.toString())
                    .setLocation("Skopje, Macedonia")
                    .setDescription("Резервации на финки");
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("America/Los_Angeles");
            event.setStart(start);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("America/Los_Angeles");
            event.setEnd(end);

            String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
            event.setRecurrence(Arrays.asList(recurrence));

            EventReminder[] reminderOverrides = new EventReminder[] {
                    new EventReminder().setMethod("email").setMinutes(24 * 60),
                    new EventReminder().setMethod("popup").setMinutes(10),
            };
            Event.Reminders reminders = new Event.Reminders()
                    .setUseDefault(false)
                    .setOverrides(Arrays.asList(reminderOverrides));
            event.setReminders(reminders);
            event = this.code.client.events().insert(calendarId, event).execute();
            System.out.printf("Event created: %s\n", event.getHtmlLink());
                Boolean proverka = this.reservationsService.makeReservation(startDateTime, endDateTime, description, u, name,event.getId() );
            return "redirect:/rooms?success";
        } catch (Exception e) {
            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.");
                String message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                        + " Redirecting to google connection status page.";
            return "redirect:/rooms?no";
        }}


    @GetMapping("/{name}")
    public ModelAndView showEditRoom(@PathVariable String name, Principal principal) {
       Room room = this.roomsService.findByName(name).get(0);

        ModelAndView modelAndView = new ModelAndView("rooms-layout");
        modelAndView.addObject("bodyContent", "edit-room");
        modelAndView.addObject("userName", principal.getName());

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
