package com.example.events.service;

import com.example.events.model.User;
import com.example.events.model.enumerations.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User register(String firstName,
                  String lastName,
                  String username,
                  String password,
                  String repeatPassword,
                  Role role);

    List<User> findAllProfessorsWithScheduledEvents();

    User findByUsername(String username);

}
