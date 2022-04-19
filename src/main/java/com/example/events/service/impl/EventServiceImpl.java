package com.example.events.service.impl;

import com.example.events.model.Event;
import com.example.events.model.EventAttendee;
import com.example.events.model.EventAttendeeKey;
import com.example.events.model.User;
import com.example.events.model.exceptions.UserNotFoundException;
import com.example.events.repository.EventAttendeeRepository;
import com.example.events.repository.EventRepository;
import com.example.events.repository.UserRepository;
import com.example.events.service.EventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventAttendeeRepository eventAttendeeRepository;

    public EventServiceImpl(EventRepository eventRepository,
                            UserRepository userRepository,
                            EventAttendeeRepository eventAttendeeRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
    }


    @Override
    public List<Event> findAll() {
        return this.eventRepository.findAll();
    }

    @Override
    public List<Event> findAllByProfessor(User professor) {
        return this.eventRepository.findAllByProfessor(professor);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return this.eventRepository.findById(id);
    }

    @Override
    public Optional<Event> save(String title,
                                String day,
                                String timeFrom,
                                String timeTo,
                                String username) {

        User user = this.userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException(username));

        Event event = new Event(title,day,timeFrom,timeTo,user,new ArrayList<>());

        return Optional.of(this.eventRepository.save(event));
    }


    @Override
    public void deleteById(Long id) {
        Set<EventAttendee> attendees = this.eventRepository.findById(id).get().getEventAttendees();

        for(EventAttendee eventAttendee : attendees) {
            this.eventAttendeeRepository.deleteById(eventAttendee.getId());
        }
        this.eventRepository.deleteById(id);
    }

    @Override
    public void attendEvent(User user, Long eventId) {
        Optional<Event> event =  this.eventRepository.findById(eventId);

        boolean isAlreadyAttending = false;

        for(User u : event.get().getAttendees()) {
            if(u.getUsername().equals(user.getUsername())) {
                isAlreadyAttending = true;
                break;
            }
        }

        if(!isAlreadyAttending) {
            EventAttendee eventAttendee = new EventAttendee();
            eventAttendee.setUser(user);
            eventAttendee.setEvent(event.get());
            eventAttendee.setTimeOfAttendAction(LocalDateTime.now());

            EventAttendeeKey eventAttendeeKey = new EventAttendeeKey();
            eventAttendeeKey.setId(event.get().getId());
            eventAttendeeKey.setUsername(user.getUsername());

            eventAttendee.setId(eventAttendeeKey);

            this.eventAttendeeRepository.save(eventAttendee);

            event.get().getEventAttendees().add(eventAttendee);
            this.eventRepository.save(event.get());
        }
    }
}
