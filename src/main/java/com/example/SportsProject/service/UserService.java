package com.example.SportsProject.service;

import com.example.SportsProject.dto.UserLoginDTO;
import com.example.SportsProject.dto.UserRegisterDTO;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationService = emailVerificationService;
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    public String signIn(UserLoginDTO userLoginDTO) {
        User userCheck = userRepository.findUserByEmail(userLoginDTO.getEmail());

        if (userCheck != null && passwordEncoder.matches(userLoginDTO.getPassword(), userCheck.getPassword())) {
            if (userCheck.getVerified()) {
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                        .withUsername(userCheck.getEmail())
                        .password(userCheck.getPassword())
                        .roles(userCheck.getRole())
                        .build();

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return "OK";
            } else if (userLoginDTO.getSendEmail() != null && userLoginDTO.getSendEmail()) {
                emailVerificationService.sendVerificationToken(userCheck);
                return "SEND_TOKEN";
            } else {
                return "NOT_VERIFIED";
            }
        } else {
            return "INCORRECT_INPUT";
        }
    }

    public Boolean signUp(UserRegisterDTO userRegisterDTO) {
        User userCheck = userRepository.findUserByEmail(userRegisterDTO.getEmail());

        if (userCheck == null){
            User user = new User();
            user.setRole("USER");
            user.setVerified(false);
            user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
            user.setName(userRegisterDTO.getName());
            user.setSurname(userRegisterDTO.getSurname());
            user.setEmail(userRegisterDTO.getEmail());
            userRepository.save(user);
            emailVerificationService.sendVerificationToken(user);
            return true;
        }

        return false;
    }
}
