package com.example.tenisv2.service;

import com.example.tenisv2.model.UserEnrollment;
import com.example.tenisv2.repository.UserEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserEnrollmentService {

    private final UserEnrollmentRepository userEnrollmentRepository;

    @Autowired
    public UserEnrollmentService(UserEnrollmentRepository userEnrollmentRepository) {
        this.userEnrollmentRepository = userEnrollmentRepository;
    }

    // Method to save user enrollment
    public UserEnrollment saveUserEnrollment(UserEnrollment userEnrollment) {
        return userEnrollmentRepository.save(userEnrollment);
    }

    // Method to retrieve all user enrollments
    public List<UserEnrollment> getAllUserEnrollments() {
        return userEnrollmentRepository.findAll();
    }

    // Method to delete user enrollment by ID
    public void deleteUserEnrollmentById(Long id) {
        userEnrollmentRepository.deleteById(id);
    }

    // You can add more service methods as needed
}
