package com.example.events.scheduled;

import com.example.events.email.EmailSenderService;
import com.example.events.model.Event;
import com.example.events.model.EventAttendee;
import com.example.events.repository.EventAttendeeRepository;
import com.example.events.service.EventService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class EventsScheduledTasks {

    private final EventService eventService;
    private final EventAttendeeRepository eventAttendeeRepository;
    private final EmailSenderService emailSenderService;

    public EventsScheduledTasks(EventService eventService,
                                EventAttendeeRepository eventAttendeeRepository,
                                EmailSenderService emailSenderService) {

        this.eventService = eventService;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.emailSenderService = emailSenderService;
    }


    @Scheduled(cron = "0 0 23 * * *", zone = "Europe/Skopje")
    public void removeAttendeesFromFinishedEvents() {

        List<EventAttendee> eventAttendees = this.eventAttendeeRepository.findAll();

        for (EventAttendee eventAttendee : eventAttendees) {

            LocalDateTime now = LocalDateTime.now();

            LocalDate localDate = now.toLocalDate();

            LocalDate localDateClickAttend = eventAttendee.getTimeOfAttendAction().toLocalDate();

            LocalTime localTimeClickAttend = eventAttendee.getTimeOfAttendAction().toLocalTime();

            if (localDate.isEqual(localDateClickAttend)) {

                int eventToHour =
                        Integer.parseInt(eventAttendee.getEvent().getTimeTo().split(":")[0]);

                int eventToMinute =
                        Integer.parseInt(eventAttendee.getEvent().getTimeTo().split(":")[1]);

                if (localTimeClickAttend.getHour() == eventToHour) {
                    if (localTimeClickAttend.getMinute() < eventToMinute) {
                        this.eventAttendeeRepository.deleteById(eventAttendee.getId());
                    }
                } else if (localTimeClickAttend.getHour() < eventToHour) {
                    this.eventAttendeeRepository.deleteById(eventAttendee.getId());
                }
            }

        }
    }

    @Scheduled(cron = "0 0 23 * * *", zone = "Europe/Skopje")
    public void sendEmailsToProfessors() {

        List<Event> events = this.eventService.findAll();

        for (Event event : events) {
            LocalDateTime now = LocalDateTime.now();

            LocalDate localDate = now.toLocalDate();

            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            if (dayOfWeek.toString().equals("MONDAY")) {
                if (event.getDay().equals("Вторник")) {
                    if (event.getEventAttendees().size() != 0) {
                        email(event);
                    }
                }
            }

            if (dayOfWeek.toString().equals("TUESDAY")) {
                if (event.getDay().equals("Среда")) {
                    if (event.getEventAttendees().size() != 0) {
                        email(event);
                    }
                }
            }

            if (dayOfWeek.toString().equals("WEDNESDAY")) {
                if (event.getDay().equals("Четврток")) {
                    if (event.getEventAttendees().size() != 0) {
                        email(event);
                    }
                }
            }

            if (dayOfWeek.toString().equals("THURSDAY")) {
                if (event.getDay().equals("Петок")) {
                    if (event.getEventAttendees().size() != 0) {
                        email(event);
                    }
                }
            }

            if (dayOfWeek.toString().equals("FRIDAY")) {
                if (event.getDay().equals("Сабота")) {
                    if (event.getEventAttendees().size() != 0) {
                        email(event);
                    }
                }
            }

            if (dayOfWeek.toString().equals("SUNDAY")) {
                if (event.getDay().equals("Понеделник")) {
                    if (event.getEventAttendees().size() != 0) {
                        email(event);
                    }
                }
            }
        }
    }

    public void email(Event event){

        String email = event.getProfessor().getUsername();
        String subject = "Настан - Пријавени учесници";

        String message = "За утрешниот настан закажан во " + event.getTimeFrom() + " часот, " +
                " пријавени се: " + event.getEventAttendees().size() + " учесници.";

        this.emailSenderService.sendEmail(email, subject, message);
    }
}
