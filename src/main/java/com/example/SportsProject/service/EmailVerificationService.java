package com.example.SportsProject.service;

import com.example.SportsProject.entity.EmailVerification;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationService {

    @Autowired
    private JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    public EmailVerificationService(EmailVerificationRepository emailVerificationRepository) {
        this.emailVerificationRepository = emailVerificationRepository;
    }

    public String sendVerificationToken(User user) {
        EmailVerification emailVerification = new EmailVerification();

        emailVerification.setUser(user);
        emailVerification.setExpiryDate(LocalDateTime.now().plusDays(1).toString());
        emailVerification.setToken(UUID.randomUUID().toString());

        emailVerificationRepository.save(emailVerification);

        String subject = "SportEqRent Confirmation token";
        String confirmationUrl = "http://localhost:8081/verify?token=" + emailVerification.getToken();
        String message = "To confirm your email, click this link:\n" + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);

        return "sendMessage(): OK";
    }
}
