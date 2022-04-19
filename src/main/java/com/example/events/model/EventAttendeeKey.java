package com.example.events.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class EventAttendeeKey implements Serializable {
    @Column(name = "username")
    private String username;

    @Column(name = "id")
    private Long id;

    public EventAttendeeKey() {
    }

    public EventAttendeeKey(String username, Long id) {
        this.username = username;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventAttendeeKey that = (EventAttendeeKey) o;
        return Objects.equals(username, that.username) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, id);
    }
}
