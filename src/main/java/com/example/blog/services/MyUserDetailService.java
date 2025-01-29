package com.example.blog.services;

import com.example.blog.domain.entities.User;
import com.example.blog.domain.repositories.UserRepository;
import com.example.blog.security.BlogUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User not found with this email "+email);
        }
        return new BlogUserDetails(user);
    }
}
