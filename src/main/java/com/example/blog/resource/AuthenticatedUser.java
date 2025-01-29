package com.example.blog.resource;

import com.example.blog.domain.entities.User;

public class AuthenticatedUser {
    private User user;
    private String token;

    public AuthenticatedUser(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}