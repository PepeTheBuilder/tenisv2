package com.example.tenisv2.repository;

import com.example.tenisv2.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findAll(); // Method to retrieve all tournaments
}
