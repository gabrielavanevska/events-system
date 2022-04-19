package com.example.events.service.impl;

import com.example.events.model.User;
import com.example.events.model.enumerations.Role;
import com.example.events.model.exceptions.InvalidUsernameOrPasswordException;
import com.example.events.model.exceptions.PasswordsDoNotMatchException;
import com.example.events.model.exceptions.UsernameAlreadyExistsException;
import com.example.events.repository.UserRepository;
import com.example.events.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String firstName, String lastName, String username,
                         String password, String repeatPassword, Role role) {

        if (username == null || username.isEmpty()  ||
                password == null || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();

        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();

        if(this.userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);

        User user = new User(username,passwordEncoder.encode(password),firstName,
                lastName,role, new ArrayList<>());
        return this.userRepository.save(user);
    }


    @Override
    public List<User> findAllProfessorsWithScheduledEvents() {
        return this.userRepository.findAllByRole(Role.ROLE_PROFESSOR)
                .stream().filter(x -> x.getScheduledEvents().size() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
