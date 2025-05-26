package com.example.SportsProject.service;

import com.example.SportsProject.entity.EmailVerification;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.EmailVerificationRepository;
import com.example.SportsProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationService {

    @Autowired
    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;

    public EmailVerificationService(JavaMailSender mailSender, EmailVerificationRepository emailVerificationRepository, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.emailVerificationRepository = emailVerificationRepository;
        this.userRepository = userRepository;
    }

    public boolean sendVerificationToken(User user) {
        EmailVerification emailVerification = new EmailVerification();

        emailVerification.setUser(user);
        emailVerification.setExpiryDate(LocalDateTime.now().plusDays(1).toString());
        emailVerification.setToken(UUID.randomUUID().toString());

        String subject = "SportEqRent Confirmation token";
        String confirmationUrl = "http://localhost:8081/verify?token=" + emailVerification.getToken();
        String message = "To confirm your email, click this link:\n" + confirmationUrl;

        try {
            userRepository.save(user);
            emailVerificationRepository.save(emailVerification);
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(user.getEmail());
            email.setSubject(subject);
            email.setText(message);

            mailSender.send(email);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
