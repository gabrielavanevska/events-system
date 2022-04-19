package com.example.events.service;

import com.example.events.model.User;

public interface AuthService {

    User login(String username, String password);

}
