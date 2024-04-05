package com.example.tenisv2.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.tenisv2.Encoder;
import com.example.tenisv2.model.Match;
import com.example.tenisv2.service.MatchService;
import com.example.tenisv2.service.UserService;
import com.example.tenisv2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MatchService matchService;

    private static User userLoggedIn;
    private boolean userLoggedInFlag = false;

    // Se pune ca single tone?
    private static final Encoder encoder = new Encoder();

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Check if username or email already exists
        if (userService.findByUsername(user.getUsername()) != null ||
                userService.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Username or email already exists");
        }

        // Set default role or validate role input
        String passEncoded = Encoder.encodingPassword(user.getPassword());
        user.setPassword(passEncoded);
        return userService.registerUser(user);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestParam String username, @RequestParam String password) {
        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(Encoder.encodingPassword(password))) {
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
    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        // Check if the user is logged in
        if (userLoggedInFlag) {
            // Update user details if any field has text
            User userToUpdate = userService.findByUsername(userLoggedIn.getUsername());

            if (userToUpdate != null) {
                System.out.println("New user: " + updatedUser);
                if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                    userToUpdate.setEmail(updatedUser.getEmail());
                }
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    userToUpdate.setPassword(Encoder.encodingPassword(updatedUser.getPassword()));
                }
                // Update other fields as needed
                userService.registerUser(userToUpdate); // Update the user in the database
                return ResponseEntity.ok(userToUpdate);
            } else {
                return ResponseEntity.notFound().build(); // User not found
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return Unauthorized status if not logged in
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logoutUser() {
        if (userLoggedInFlag) {
            userLoggedInFlag = false;
            userLoggedIn = null;
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No user logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    @GetMapping("/match_export")
    public ResponseEntity<String> exportMatchData() {
        // Assuming you have a way to retrieve current user details (e.g., from session or token)
        if(userLoggedInFlag){
            switch (userLoggedIn.getRole()) {
                case "admin" -> {
                    // Export match data
                    return ResponseEntity.ok("Match data exported successfully");
                }
                case "user" -> {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized to export match data");
                }
                case "referee" -> {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Referee not authorized to export match data");
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
    }
    @PutMapping("/update_user") // by admin
    public ResponseEntity<User> updateUserByAdmin(@RequestBody User updatedUser) {
        User existingUser = null;
        System.out.println("New user: " + updatedUser);
        if (updatedUser.getId() != null) {
            // Update user based on ID
            existingUser = userService.findById(updatedUser.getId());
        } else if (updatedUser.getUsername() != null) {
            // Update user based on username
            existingUser = userService.findByUsername(updatedUser.getUsername());
        }

        if (existingUser != null) {
            // Update user details
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setRole(updatedUser.getRole());
            // Set other fields to update as needed
            // Save the updated user

            userService.registerUser(existingUser);

            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build(); // User not found
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        System.out.printf("User id to delete: %d\n", userId);
        userService.deleteById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
    @GetMapping("/user_managmement")
    public ResponseEntity<String> manageUsers() {
        // Assuming you have a way to retrieve current user details (e.g., from session or token)
        if(userLoggedInFlag){
            if (userLoggedIn.getRole().equals("admin")) {
                    // Manage users
                    return ResponseEntity.ok("User management page");
                }
                else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not admin");
                }
            }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
    }

    @GetMapping("/matches")
    public ResponseEntity<List<Match>> getRefereeMatches() {

        if (userLoggedInFlag && userLoggedIn.getRole().equals("referee")) {
            List<Match> refereeMatches = matchService.findByRefereeId(userLoggedIn.getId());
            return ResponseEntity.ok(refereeMatches);
        } else {
            return ResponseEntity.notFound().build(); // Referee not found or unauthorized
        }
    }

    @PostMapping("/score")
    public ResponseEntity<String> updateMatchScore(@RequestParam Long matchId, @RequestParam String score) {
        // Get the current user
        if (userLoggedInFlag && userLoggedIn.getRole().equals("referee")) {
            List<Match> matches = matchService.findByRefereeId(userLoggedIn.getId());
            Match matchToUpdate = null;
            for (Match match : matches) {
                if (match.getId().equals(matchId)) {
                    matchToUpdate = match;
                    break;
                }
            }
            if (matchToUpdate != null) {
                matchToUpdate.setScore(score);
                matchService.updateMatch(matchToUpdate);
                return ResponseEntity.ok("Match score updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Match>> searchMatches(@RequestParam(required = false) String playerName,
                                                     @RequestParam(required = false) String matchDate) {
        List<Match> filteredMatches;
        if (playerName != null && !playerName.isEmpty()) {
            // Search matches by player name
            filteredMatches = matchService.findByPlayerName(playerName);
        } else if (matchDate != null && !matchDate.isEmpty()) {
            // Search matches by match date
            filteredMatches = matchService.findByMatchDate(matchDate);

        } else {
            // If no search criteria provided, return all matches
            filteredMatches = matchService.getAllMatches();
        }
        return ResponseEntity.ok(filteredMatches);
    }
    @GetMapping("/save/csv")
    public ResponseEntity<String> saveMatchesAsCSV() {
        List<Match> matches = matchService.getAllMatches();
        String filename = "matches.csv";
        try (FileWriter writer = new FileWriter(filename)) {
            // Write CSV header
            writer.append("ID,Tournament ID,Player 1 ID,Player 2 ID,Referee ID,Match Date,Score\n");
            // Write match data
            for (Match match : matches) {
                writer.append(match.getId() + "," + match.getTournamentId() + "," + match.getPlayer1Id() +
                        "," + match.getPlayer2Id() + "," + match.getRefereeId() + "," + match.getMatchDate() +
                        "," + match.getScore() + "\n");
            }
            return ResponseEntity.ok("Matches saved as CSV file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save matches as CSV file");
        }
    }
    @GetMapping("/save/txt")
    public ResponseEntity<String> saveMatchesAsTXT() {
        List<Match> matches = matchService.getAllMatches();
        String filename = "matches.txt";
        try (FileWriter writer = new FileWriter(filename)) {
            // Write match data
            for (Match match : matches) {
                writer.append("ID: " + match.getId() + "\n");
                writer.append("Tournament ID: " + match.getTournamentId() + "\n");
                writer.append("Player 1 ID: " + match.getPlayer1Id() + "\n");
                writer.append("Player 2 ID: " + match.getPlayer2Id() + "\n");
                writer.append("Referee ID: " + match.getRefereeId() + "\n");
                writer.append("Match Date: " + match.getMatchDate() + "\n");
                writer.append("Score: " + match.getScore() + "\n\n");
            }
            return ResponseEntity.ok("Matches saved as TXT file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save matches as TXT file");
        }
    }
    // Endpoint for tennis players to view their scheduled matches
    @GetMapping("/byRole")
    public ResponseEntity<List<Match>> getMatchesForTennisPlayer() {
        // Logic to retrieve matches scheduled for the logged-in tennis player
        // Assuming you have a method in MatchService for this purpose
//        System.out.println(userLoggedInFlag+" ceva se intampla player "+userLoggedIn.getRole()+"\n");
        if (!userLoggedInFlag) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return switch (userLoggedIn.getRole()) {
            case "tennis_player" -> {
                List<Match> playerMatches = matchService.findByPlayerId(userLoggedIn.getId());
                System.out.println( playerMatches + " - player \n");
                yield ResponseEntity.ok(playerMatches);
            }
            case "referee" -> {
                List<Match> refereeMatches = matchService.findByRefereeId(userLoggedIn.getId());

                System.out.println( refereeMatches + " - referee \n");
                yield ResponseEntity.ok(refereeMatches);
            }
            default -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };

    }
}
