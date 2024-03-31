package com.example.tenisv2.service;

import com.example.tenisv2.model.User;
import com.example.tenisv2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // Perform any additional validation if necessary
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getCurrentUserFromSessionOrToken() {
        // Retrieve current user details based on session or token
        // Example implementation (assuming session-based authentication):
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userRepository.findByUsername(username);
        }
        return null; // No authenticated user found
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}