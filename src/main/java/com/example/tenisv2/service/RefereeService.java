package com.example.tenisv2.service;

import com.example.tenisv2.model.Match;
import com.example.tenisv2.repository.MatchRepository;
import com.example.tenisv2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RefereeService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;

/*    public List<Match> getRefereeMatches(Long refereeId) {
        return matchRepository.findByRefereeId(refereeId);
    }*/

    public void updateMatchScore(Long matchId, String score) {
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        matchOptional.ifPresent(match -> {
            match.setScore(score);
            matchRepository.save(match);
        });
    }
}
