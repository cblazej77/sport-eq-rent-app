package com.example.SportsProject.controller;

import com.example.SportsProject.dto.UserLoginDTO;
import com.example.SportsProject.dto.UserRegisterDTO;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.UserRepository;
import com.example.SportsProject.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void shouldShowLoginForm() throws Exception {
        mockMvc.perform(get("/sign_in"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign_in"))
                .andExpect(model().attributeExists("userLoginDTO"));
    }

    @Test
    public void shouldShowRegisterForm() throws Exception {
        mockMvc.perform(get("/sign_up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign_up"))
                .andExpect(model().attributeExists("userRegisterDTO"));
    }

    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        when(userService.signUp(any(UserRegisterDTO.class))).thenReturn("success");

        mockMvc.perform(post("/sign_up_action")
                        .param("email", "user@example.com")
                        .param("password", "pass1234")
                        .param("name", "Test")
                        .param("surname", "User"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sign_in?message=registered"));
    }

    @Test
    public void shouldFailRegisterUser() throws Exception {
        when(userService.signUp(any(UserRegisterDTO.class))).thenReturn("email_exists");
        when(messageSource.getMessage(Mockito.any(), Mockito.any(), Mockito.any(Locale.class))).thenReturn("Błąd rejestracji");

        mockMvc.perform(post("/sign_up_action")
                        .param("email", "user@example.com")
                        .param("password", "pass1234")
                        .param("name", "Test")
                        .param("surname", "User"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign_up"))
                .andExpect(model().attributeExists("registerError"));
    }

    @Test
    public void shouldSignInSuccessfully() throws Exception {
        when(userService.signIn(any(UserLoginDTO.class), any(AuthenticationManager.class))).thenReturn("OK");

        mockMvc.perform(post("/sign_in_action")
                        .param("email", "user@example.com")
                        .param("password", "pass1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void shouldHandleIncorrectInput() throws Exception {
        when(userService.signIn(any(UserLoginDTO.class), any(AuthenticationManager.class))).thenReturn("INCORRECT_INPUT");
        when(messageSource.getMessage(Mockito.any(), Mockito.any(), Mockito.any(Locale.class))).thenReturn("Nieprawidłowe dane");

        mockMvc.perform(post("/sign_in_action")
                        .param("email", "wrong@example.com")
                        .param("password", "wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign_in"))
                .andExpect(model().attributeExists("loginError"));
    }
}
