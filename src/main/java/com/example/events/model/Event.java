package com.example.events.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String day;

    private String timeFrom;

    private String timeTo;

    @JsonIgnore
    @ManyToOne
    private User professor;
    @JsonIgnore
    @ManyToMany
    private List<User> attendees;


    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private Set<EventAttendee> eventAttendees;


    public Event() {
    }

    public Event(String title,
                 String day,
                 String timeFrom,
                 String timeTo,
                 User professor,
                 List<User> attendees) {
        this.title = title;
        this.day = day;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.professor = professor;
        this.attendees = attendees;
    }
}
