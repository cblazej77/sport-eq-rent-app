package com.example.SportsProject.service;

import com.example.SportsProject.entity.EmailVerification;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.EmailVerificationRepository;
import com.example.SportsProject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailVerificationService emailVerificationService;

    @Captor
    ArgumentCaptor<EmailVerification> verificationCaptor;

    @Captor
    ArgumentCaptor<SimpleMailMessage> emailCaptor;

    @Test
    void sendVerificationToken() {
        User user = new User();
        user.setEmail("user@example.com");

        boolean result = emailVerificationService.sendVerificationToken(user);

        assertTrue(result);

        verify(userRepository).save(user);

        verify(emailVerificationRepository).save(verificationCaptor.capture());
        EmailVerification saved = verificationCaptor.getValue();
        assertEquals(user, saved.getUser());
        assertNotNull(saved.getToken());
        assertNotNull(saved.getExpiryDate());

        verify(mailSender).send(emailCaptor.capture());
        SimpleMailMessage sentEmail = emailCaptor.getValue();
        assertEquals("user@example.com", sentEmail.getTo()[0]);
        assertEquals("SportEqRent Confirmation token", sentEmail.getSubject());
        assertTrue(sentEmail.getText().contains("http://localhost:8081/verify?token=" + saved.getToken()));
    }

}