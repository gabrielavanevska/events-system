package com.example.events.web;

import com.example.events.model.Event;
import com.example.events.model.EventAttendee;
import com.example.events.model.User;
import com.example.events.model.enumerations.Role;
import com.example.events.repository.EventAttendeeRepository;
import com.example.events.service.EventService;
import com.example.events.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final EventAttendeeRepository eventAttendeeRepository;

    public EventController(EventService eventService,
                           UserService userService,
                           EventAttendeeRepository eventAttendeeRepository) {
        this.eventService = eventService;
        this.userService = userService;
        this.eventAttendeeRepository = eventAttendeeRepository;
    }

    @GetMapping(value = "/")
    public String getLandingPage() {
        return "landingPage";
    }

    @GetMapping(value = "/UserOrProfessor")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_PROFESSOR')")
    public String UserOrProfessor(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if (user.getRole().equals(Role.ROLE_PROFESSOR)) {
            return "redirect:/professor";
        } else if (user.getRole().equals(Role.ROLE_USER)) {
            return "redirect:/user";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/professor")
//    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public String getProfessorAfterLoginPage(
            @RequestParam(required = false) String error,
            HttpServletRequest request,
            Authentication authentication,
            Model model) {

        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        String username = request.getRemoteUser();
        User professor = (User) authentication.getPrincipal();
        List<Event> events = this.eventService.findAllByProfessor(professor);

        model.addAttribute("events", events);
        model.addAttribute("username", username);


        return "professor_events";
    }

    @GetMapping(value = "/user")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public String getStudentsAfterLoginPage(
            @RequestParam(required = false) String error,
            Authentication authentication,
            Model model) {

        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        User student = (User) authentication.getPrincipal();
        List<User> professors = this.userService.findAllProfessorsWithScheduledEvents();

        model.addAttribute("professors", professors);
        model.addAttribute("username", student.getFirstName());


        return "user_events";
    }

    @GetMapping(value = "/listOfEvents/{id}")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public String getEvents(@RequestParam(required = false) String error,
                            @PathVariable String id,
                            Principal principal,
                            Model model) {

        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        User user = this.userService.findByUsername(principal.getName());
        User professor = (User) this.userService.loadUserByUsername(id);
        List<Event> events = professor.getScheduledEvents();

        for (Event event : events) {
            List<User> attendees = event.getEventAttendees()
                    .stream().map(EventAttendee::getUser)
                    .collect(Collectors.toList());

            event.setAttendees(attendees);
        }

        model.addAttribute("events", events);
        model.addAttribute("professor", professor);
        model.addAttribute("username", user.getFirstName());
        model.addAttribute("user", user);

        return "attend_event";
    }

    @PostMapping(value = "/attendEvent/{id}")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public String attendEvent(@PathVariable Long id,
                              Principal principal) {

        User user = this.userService.findByUsername(principal.getName());

        this.eventService.attendEvent(user, id);

        Event event = this.eventService.findById(id).get();
        String professorId = event.getProfessor().getUsername();

        return "redirect:/listOfEvents/" + professorId;
    }

    @PostMapping(value = "/deleteAttendEvent/{id}")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteAttendEvent(@PathVariable Long id,
                                    @RequestParam(required = false) String actionSource,
                                    Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Event event = this.eventService.findById(id).get();

        event.getAttendees().removeIf(x -> x.getUsername().equals(user.getUsername()));

        EventAttendee eventAttendee = event.getEventAttendees()
                .stream()
                .filter(x -> x.getUser().getUsername().equals(user.getUsername()))
                .findFirst().get();

        this.eventAttendeeRepository.delete(eventAttendee);

        String professorId = event.getProfessor().getUsername();

        if (actionSource != null && actionSource.equals("MY_EVENTS")) {
            return "redirect:/myEvents";
        }

        return "redirect:/listOfEvents/" + professorId;
    }

    @GetMapping("/myEvents")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public String myEvents(Principal principal, Model model) {

        User user = this.userService.findByUsername(principal.getName());

        Set<EventAttendee> eventAttendees = user.getEventAttendees();

        model.addAttribute("eventAttendees", eventAttendees);
        model.addAttribute("username", user.getFirstName());

        return "my_events";
    }

    @PostMapping(value = "/professor")
//    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public String scheduleEvent(
            @RequestParam String title,
            @RequestParam String day,
            @RequestParam String timeFrom,
            @RequestParam String timeTo,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        this.eventService.save(title, day, timeFrom, timeTo, user.getUsername());

        return "redirect:/professor";
    }

    @PostMapping(value = "/professor/delete/{id}")
//    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public String deleteEvent(@PathVariable Long id) {

        this.eventService.deleteById(id);
        return "redirect:/professor";

    }

}
