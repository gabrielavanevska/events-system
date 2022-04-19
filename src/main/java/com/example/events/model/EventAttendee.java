package com.example.events.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class EventAttendee {
    @EmbeddedId
    EventAttendeeKey id;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "username")
    User user;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "id")
    Event event;

    LocalDateTime timeOfAttendAction;

    public EventAttendee() {
    }

    public EventAttendee(User user,
                         Event event,
                         LocalDateTime timeOfAttendAction) {
        this.user = user;
        this.event = event;
        this.timeOfAttendAction = timeOfAttendAction;
    }

    public EventAttendeeKey getId() {
        return id;
    }

    public void setId(EventAttendeeKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getTimeOfAttendAction() {
        return timeOfAttendAction;
    }

    public void setTimeOfAttendAction(LocalDateTime timeOfAttendAction) {
        this.timeOfAttendAction = timeOfAttendAction;
    }
}
