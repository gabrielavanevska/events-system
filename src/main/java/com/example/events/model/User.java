package com.example.events.model;

import com.example.events.model.enumerations.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Table(name = "EVENT_USER")
@Entity
public class User implements UserDetails {
    @Id
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<EventAttendee> eventAttendees;

    @OneToMany(mappedBy = "professor")
    private List<Event> scheduledEvents;

    private boolean isAccountNonExpired = true;

    private boolean isAccountNonLocked = true;

    private boolean isCredentialsNonExpired = true;

    private boolean isEnabled = true;

    public User() {
    }

    public User(String username,
                String password,
                String firstName,
                String lastName,
                Role role,
                List<Event> scheduledEvents) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.scheduledEvents = scheduledEvents;
    }
    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<EventAttendee> getEventAttendees() {
        return eventAttendees;
    }

    public void setEventAttendees(Set<EventAttendee> eventAttendees) {
        this.eventAttendees = eventAttendees;
    }

    public List<Event> getScheduledEvents() {
        return scheduledEvents;
    }

    public void setScheduledEvents(List<Event> scheduledEvents) {
        this.scheduledEvents = scheduledEvents;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
