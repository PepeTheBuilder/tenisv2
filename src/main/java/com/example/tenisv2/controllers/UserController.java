package com.example.tenisv2.controllers;

import java.util.HashMap;
import java.util.Map;

import com.example.tenisv2.service.UserService;
import com.example.tenisv2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    private User userLoggedIn;
    private boolean userLoggedInFlag = false;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Check if username or email already exists
        if (userService.findByUsername(user.getUsername()) != null ||
                userService.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Username or email already exists");
        }

        // Set default role or validate role input

        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestParam String username, @RequestParam String password) {
        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            userLoggedIn= user;
            userLoggedInFlag = true;
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            // You can add additional information to the response such as user details or a session token
            return ResponseEntity.ok(response);
        } else {
            userLoggedInFlag = false;
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        // Assuming you have a way to retrieve current user details (e.g., from session or token)
        if(userLoggedInFlag){
            return ResponseEntity.ok(userLoggedIn);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        // Assuming you have a way to retrieve current user details (e.g., from session or token)
        if(userLoggedInFlag){
            switch (userLoggedIn.getRole()) {
                case "admin" -> {
                    List<User> allUsers = userService.getAllUsers();
                    return ResponseEntity.ok(allUsers);
                }
                case "user" -> {
                    ///TODO: Implement Match classes and methods for user
                    List<User> placeholder = List.of(new User(-1L, "user", "", "user", "user"));
                    return ResponseEntity.ok(placeholder);
                }
                case "referee" -> {
                    ///TODO: Implement Match classes and methods for referee
                    List<User> placeholder = List.of(new User(-1L, "referee", "", "referee", "referee"));
                    return ResponseEntity.ok(placeholder);
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return Unauthorized status if not logged in
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return Forbidden status if not admin
        }
    }
}
