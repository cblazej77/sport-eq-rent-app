package com.example.SportsProject.controller;

import com.example.SportsProject.entity.EmailVerification;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.EmailVerificationRepository;
import com.example.SportsProject.repository.UserRepository;
import com.example.SportsProject.service.EmailVerificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailVerificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmailVerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailVerificationService emailVerificationService;

    @MockBean
    private EmailVerificationRepository emailVerificationRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void verifyEmail(){
        String token = "valid-token";

        User user = new User();
        user.setVerified(false);

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setToken(token);
        emailVerification.setExpiryDate(LocalDateTime.now().plusDays(1).toString()); // ważny token
        emailVerification.setUser(user);

        when(emailVerificationRepository.findByToken(token)).thenReturn(emailVerification);
        when(userRepository.save(any(User.class))).thenReturn(user);

        try {
            mockMvc.perform(get("/verify")
                            .param("token", token))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/sign_in?message=verified"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertTrue(user.getVerified());
        verify(userRepository).save(user);
    }
}