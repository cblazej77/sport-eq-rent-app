package com.example.SportsProject.controller;

import com.example.SportsProject.entity.EmailVerification;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.EmailVerificationRepository;
import com.example.SportsProject.repository.UserRepository;
import com.example.SportsProject.service.EmailVerificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;

    public EmailVerificationController(EmailVerificationService emailVerificationService, EmailVerificationRepository emailVerificationRepository, UserRepository userRepository) {
        this.emailVerificationService = emailVerificationService;
        this.emailVerificationRepository = emailVerificationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        EmailVerification emailVerification = emailVerificationRepository.findByToken(token);

        if (emailVerification != null && LocalDateTime.parse(emailVerification.getExpiryDate()).isAfter(LocalDateTime.now())) {
            User user = emailVerification.getUser();
            user.setVerified(true);
            userRepository.save(user);

            return "redirect:/sign_in?message=verified";
        } else {
            return "redirect:/categories";
        }
    }
}
