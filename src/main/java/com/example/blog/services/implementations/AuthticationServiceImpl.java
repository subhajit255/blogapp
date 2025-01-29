package com.example.blog.services.implementations;

import com.example.blog.domain.entities.User;
import com.example.blog.domain.repositories.UserRepository;
import com.example.blog.resource.AuthenticatedUser;
import com.example.blog.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthticationServiceImpl implements AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthticationServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

//    @Override
//    public String verify(User user) {
//        System.out.println("user ============>=="+user);
//        try {
//            // Create an authentication token using email and password
//            Authentication authentication = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
//            );
//            if(authentication.isAuthenticated()){
//                System.out.println("hereee");
//                System.out.println("token ===========>"+jwtService.generateToken(user.getEmail()));
//                return jwtService.generateToken(user.getEmail());
//            }else{
//                return "fail";
//            }
//        } catch (AuthenticationException e) {
//            // Handle authentication failure
//            return "fail";
//        }
//
//    }

    public AuthenticatedUser verify(User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Invalid user or missing credentials");
        }
        System.out.println("user ============>" + user);

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                System.out.println("Authentication successful");

                String token = jwtService.generateToken(user.getEmail());
                System.out.println("Generated token ===========> " + token);
                User getUser = userRepository.findByEmail(user.getEmail());
                return new AuthenticatedUser(getUser, token);
            } else {
                throw new RuntimeException("Authentication failed");
            }
        } catch (AuthenticationException e) {
            System.out.println("Authentication exception: " + e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        }
    }

    @Override
    public List<User> users() {
        List<User> userList = userRepository.findAll();
        if (!userList.isEmpty()) {
            return userList; // Return the first user
        } else {
            throw new RuntimeException("No users found"); // Handle empty list case
        }
    }

}
