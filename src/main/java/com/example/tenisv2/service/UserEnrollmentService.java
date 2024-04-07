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

    public UserEnrollment saveUserEnrollment(UserEnrollment userEnrollment) {
        return userEnrollmentRepository.save(userEnrollment);
    }

    public List<UserEnrollment> getAllUserEnrollments() {
        return userEnrollmentRepository.findAll();
    }

    public void deleteUserEnrollmentById(Long id) {
        userEnrollmentRepository.deleteById(id);
    }

}
