package com.example.tenisv2;

import java.util.HashMap;
import java.util.Map;

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
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            // You can add additional information to the response such as user details or a session token
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        // Assuming you have a way to retrieve current user details (e.g., from session or token)
        User currentUser = userService.getCurrentUserFromSessionOrToken(); // Implement this method as per your application logic
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        // Assuming you have a way to retrieve current user details (e.g., from session or token)
        User currentUser = userService.getCurrentUserFromSessionOrToken(); // Implement this method as per your application logic
        if (currentUser != null && currentUser.getRole().equals("admin")) {
            List<User> allUsers = userService.getAllUsers();
            return ResponseEntity.ok(allUsers);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return Forbidden status if not admin
        }
    }
}
