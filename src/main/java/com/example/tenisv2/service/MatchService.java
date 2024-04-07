package com.example.tenisv2.service;
import com.example.tenisv2.model.Match;
import com.example.tenisv2.model.User;
import com.example.tenisv2.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserService userService;

    public List<Match>  saveMatch(Match match) {
        return Collections.singletonList(matchRepository.save(match));
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public void deleteById(Long matchId) {
        matchRepository.deleteById(matchId);
    }

    public List<Match> findByPlayerId(Long playerId) {
        return matchRepository.findByPlayer1Id(playerId);
    }

    public List<Match> findByPlayerName(String playerName) {
        User player = userService.findByUsername(playerName);
        List<Match> matches = matchRepository.findByPlayer1Id(player.getId());
        List<Match> matches2 = matchRepository.findByPlayer2Id(player.getId());

        matches.addAll(matches2);

        return matches;

    }

    public List<Match> findByMatchDate(String matchDate) {
        return matchRepository.findByMatchDate(matchDate);
    }

    public List<Match> findByRefereeId(Long refereeId) {
        return matchRepository.findByRefereeId(refereeId);
    }

    public void updateMatch(Match matchToUpdate) {
        matchRepository.save(matchToUpdate);
    }

    public void updateMatchScore(Long matchId, String score) {
        Match match = matchRepository.findById(matchId).orElseThrow();
        match.setScore(score);
        matchRepository.save(match);
    }

}
