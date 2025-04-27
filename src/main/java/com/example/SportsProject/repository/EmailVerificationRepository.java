package com.example.SportsProject.repository;

import com.example.SportsProject.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    EmailVerification findByToken(String token);
}
