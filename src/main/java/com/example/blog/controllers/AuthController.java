package com.example.blog.controllers;

import com.example.blog.domain.dtos.AuthResponse;
import com.example.blog.domain.dtos.LoginRequest;
import com.example.blog.domain.entities.User;
import com.example.blog.resource.AuthenticatedUser;
import com.example.blog.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController extends BaseController{

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            // Call the service to register the user
            User registeredUser = authenticationService.register(user);
            return new ResponseEntity<>(
                    registeredUser,
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            // Return a 400 Bad Request response in case of an error
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Authenticate user and get AuthenticatedUser object
            AuthenticatedUser authResponse = authenticationService.verify(user);
            System.out.println(authResponse);

            // Return success response
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "Login successful",
                    "data", authResponse
            ));
        } catch (RuntimeException ex) {
            // Handle authentication failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", false,
                    "message", "Invalid credentials",
                    "details", ex.getMessage()
            ));
        } catch (Exception ex) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", false,
                    "message", "An unexpected error occurred",
                    "details", ex.getMessage()
            ));
        }
    }

    @GetMapping("/users")
    public List<User> users(){
        return authenticationService.users();
    }
}
