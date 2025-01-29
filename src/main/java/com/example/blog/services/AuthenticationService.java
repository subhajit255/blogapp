package com.example.blog.services;

import com.example.blog.domain.entities.User;
import com.example.blog.resource.AuthenticatedUser;

import java.util.List;

public interface AuthenticationService {

    User register(User user);

    AuthenticatedUser verify(User user);

    List<User> users();
}
