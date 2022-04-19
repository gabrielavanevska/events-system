package com.example.events.service;

import com.example.events.model.Event;
import com.example.events.model.User;

import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> findAll();

    List<Event> findAllByProfessor(User professor);

    Optional<Event> findById(Long id);

    Optional<Event> save(String title,
                         String day,
                         String timeFrom,
                         String timeTo,
                         String username);

    void deleteById(Long id);

    void attendEvent(User user, Long eventId);

}
