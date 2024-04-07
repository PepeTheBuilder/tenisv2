package com.example.tenisv2.repository;

import com.example.tenisv2.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByPlayer2Id(long player2);

    List<Match> findByPlayer1Id(long player1);

    List<Match> findByMatchDate(String matchDate);

    List<Match> findByRefereeId(Long refereeId);


}
