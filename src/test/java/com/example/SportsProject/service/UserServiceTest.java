package com.example.SportsProject.service;

import com.example.SportsProject.dto.UserLoginDTO;
import com.example.SportsProject.dto.UserRegisterDTO;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.service.EmailVerificationService;
import com.example.SportsProject.service.UserService;
import com.example.SportsProject.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void signIn_userNotFound_returnsIncorrectInput() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("notfound@example.com");

        when(userRepository.findUserByEmail("notfound@example.com")).thenReturn(null);

        String result = userService.signIn(dto, authenticationManager);

        assertEquals("INCORRECT_INPUT", result);
    }

    @Test
    void signIn_userNotVerified_sendEmailTrue_sendsToken() {
        User user = new User();
        user.setVerified(false);

        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@example.com");
        dto.setSendEmail(true);

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(user);

        String result = userService.signIn(dto, authenticationManager);

        verify(emailVerificationService).sendVerificationToken(user);
        assertEquals("SEND_TOKEN", result);
    }

    @Test
    void signIn_userNotVerified_sendEmailFalse_returnsNotVerified() {
        User user = new User();
        user.setVerified(false);

        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@example.com");
        dto.setSendEmail(false);

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(user);

        String result = userService.signIn(dto, authenticationManager);

        verify(emailVerificationService, never()).sendVerificationToken(any());
        assertEquals("NOT_VERIFIED", result);
    }

    @Test
    void signIn_userVerified_authenticationSuccess_returnsOk() {
        User user = new User();
        user.setVerified(true);

        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("password");

        Authentication auth = mock(Authentication.class);

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        String result = userService.signIn(dto, authenticationManager);

        assertEquals("OK", result);
        assertEquals(auth, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void signIn_userVerified_authenticationFails_returnsIncorrectInput() {
        User user = new User();
        user.setVerified(true);

        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("wrongpass");

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationServiceException("fail"));

        String result = userService.signIn(dto, authenticationManager);

        assertEquals("INCORRECT_INPUT", result);
    }

    @Test
    void signUp_shouldReturnSuccess() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        dto.setName("Jan");
        dto.setSurname("Kowalski");

        when(userRepository.findUserByEmail(dto.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(emailVerificationService.sendVerificationToken(any(User.class))).thenReturn(true);

        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setUserID(1L);
            return null;
        }).when(userRepository).save(any(User.class));

        String result = userService.signUp(dto);

        assertEquals("success", result);

        verify(userRepository).save(any(User.class));
        verify(emailVerificationService).sendVerificationToken(any(User.class));
    }
}
