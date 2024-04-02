package com.example.tenisv2.controllers;

import com.example.tenisv2.model.Match;
import com.example.tenisv2.model.User;
import com.example.tenisv2.service.MatchService;
import com.example.tenisv2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/referee")
public class RefereeController {
    @Autowired
    private MatchService matchService;

    @Autowired
    private UserService userService;

    @GetMapping("/matches")
    public ResponseEntity<List<Match>> getRefereeMatches() {
        // Get the current user
        User currentUser = getCurrentUser();
        if (currentUser != null && currentUser.getRole().equals("referee")) {
            List<Match> refereeMatches = matchService.findByRefereeId(currentUser.getId());
            return ResponseEntity.ok(refereeMatches);
        } else {
            return ResponseEntity.notFound().build(); // Referee not found or unauthorized
        }
    }

    @PostMapping("/score")
    public ResponseEntity<String> updateMatchScore(@RequestParam Long matchId, @RequestParam String score) {
        // Get the current user
        User currentUser = getCurrentUser();
        if (currentUser != null && currentUser.getRole().equals("referee")) {
            List<Match> matches = matchService.findByRefereeId(currentUser.getId());
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

    // Method to retrieve the current user (assuming user authentication)
    private User getCurrentUser() {
        // Implement this method to retrieve the current user based on the session or token
        // Return null if the user is not authenticated
        return null;
    }
}

