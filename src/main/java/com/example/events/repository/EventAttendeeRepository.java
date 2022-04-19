package com.example.events.repository;

import com.example.events.model.EventAttendee;
import com.example.events.model.EventAttendeeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, EventAttendeeKey> {
}
