package com.models;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.web.controllers.GoogleCalController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class Code {
    public  String code = "";
    public final static Log logger = LogFactory.getLog(GoogleCalController.class);
    public static final String APPLICATION_NAME = "";
    public static HttpTransport httpTransport;
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static com.google.api.services.calendar.Calendar client;

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    @Value("${google.client.client-id}")
    public String clientId;
    @Value("${google.client.client-secret}")
    public String clientSecret;
    @Value("${google.client.redirectUri}")
    public String redirectURI;
    public String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            this.clientSecrets = new GoogleClientSecrets().setWeb(web);
            this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            this.flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR)).build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
        System.out.println("cal authorizationUrl->" + authorizationUrl);
        return authorizationUrl.build();
    }
    public void credentials(String code) throws IOException {
        TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
        this.credential = flow.createAndStoreCredential(response, "userID");
// Initialize Calendar client with valid OAuth credentials
       this.client = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName("applicationName").build();
    }
}













//    @RequestMapping(value = "/login/google", method = RequestMethod.GET, params = "code")
//    public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code) {
//        com.google.api.services.calendar.model.Events eventList;
//        String message;
//        try {
//            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
//            credential = flow.createAndStoreCredential(response, "userID");
//            client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
//                    .setApplicationName(APPLICATION_NAME).build();
//            com.google.api.services.calendar.Calendar.Events events = client.events();
//            eventList = events.list("primary").setTimeMin(date1).setTimeMax(date2).execute();
//            message = eventList.getItems().toString();
//            System.out.println("My:" + eventList.getItems());
//            Event event = new Event()
//                    .setSummary("Google I/O 2015")
//                    .setLocation("800 Howard St., San Francisco, CA 94103")
//                    .setDescription("A chance to hear more about Google's developer products.");
//
//
//            //   Initialize Calendar service with valid OAuth credentials
//            com.google.api.services.calendar.Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
//                    .setApplicationName("applicationName").build();
//
//// Create a new calendar
//            com.google.api.services.calendar.model.Calendar calendar1 = new com.google.api.services.calendar.model.Calendar();
//            calendar1.setSummary("calendarSummary");
//            calendar1.setTimeZone("America/Los_Angeles");
//
//// Insert the new calendar
//            com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar1).execute();
//
//            System.out.println(createdCalendar.getId());
//
//
//            DateTime startDateTime = new DateTime("2020-01-11T09:00:00-07:00");
//            EventDateTime start = new EventDateTime()
//                    .setDateTime(startDateTime)
//                    .setTimeZone("America/Los_Angeles");
//            event.setStart(start);
//
//            DateTime endDateTime = new DateTime("2020-01-11T17:00:00-07:00");
//            EventDateTime end = new EventDateTime()
//                    .setDateTime(endDateTime)
//                    .setTimeZone("America/Los_Angeles");
//            event.setEnd(end);
//
//            String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
//            event.setRecurrence(Arrays.asList(recurrence));
//
////            EventAttendee[] attendees = new EventAttendee[] {
////                    new EventAttendee().setEmail("lpage@example.com"),
////                    new EventAttendee().setEmail("sbrin@example.com"),
////            };
////            event.setAttendees(Arrays.asList(attendees));
//
//            EventReminder[] reminderOverrides = new EventReminder[] {
//                    new EventReminder().setMethod("email").setMinutes(24 * 60),
//                    new EventReminder().setMethod("popup").setMinutes(10),
//            };
//            Event.Reminders reminders = new Event.Reminders()
//                    .setUseDefault(false)
//                    .setOverrides(Arrays.asList(reminderOverrides));
//            event.setReminders(reminders);
//
//            String calendarId = "1hu2h4iu8pgklo6erctabcegp8@group.calendar.google.com";
//            event = client.events().insert(calendarId, event).execute();
//            System.out.printf("Event created: %s\n", event.getHtmlLink());
//        } catch (Exception e) {
//            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
//                    + " Redirecting to google connection status page.");
//            message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
//                    + " Redirecting to google connection status page.";
//        }
//
//        System.out.println("cal message:" + message);
//        return new ResponseEntity<>(message, HttpStatus.OK);
//    }