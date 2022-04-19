package com.example.events.model.enumerations;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_PROFESSOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
