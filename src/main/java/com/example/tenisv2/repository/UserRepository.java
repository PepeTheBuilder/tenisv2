package com.example.tenisv2.repository;

import com.example.tenisv2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    void deleteById(Long id);
}
