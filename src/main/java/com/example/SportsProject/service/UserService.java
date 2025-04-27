package com.example.SportsProject.service;

import com.example.SportsProject.dto.UserLoginDTO;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Boolean signIn(UserLoginDTO userLoginDTO) {
        User userCheck = userRepository.findUserByEmail(userLoginDTO.getEmail());
        if (userCheck != null && userCheck.getVerified() && passwordEncoder.matches(userLoginDTO.getPassword(), userCheck.getPassword())) {
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(userCheck.getEmail())
                    .password(userCheck.getPassword())
                    .roles(userCheck.getRole())
                    .build();

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("Sign OK: " + authenticationToken);
            return true;
        }
        System.out.println("Sign Err");
        return false;
    }

    public String signUp(User user) {
        User userCheck = userRepository.findUserByEmail(user.getEmail());

        if (userCheck == null){
            user.setRole("USER");
            user.setVerified(false);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            emailVerificationService.sendVerificationToken(user);
            return "User saved";
        }

        return "User already exists";
    }
}
