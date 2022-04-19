package com.example.events.repository;

import com.example.events.model.Event;
import com.example.events.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findAllByProfessor(User professor);
}
