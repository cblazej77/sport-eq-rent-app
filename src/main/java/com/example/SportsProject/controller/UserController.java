package com.example.SportsProject.controller;

import com.example.SportsProject.dto.UserLoginDTO;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign_in")
    public String sign_in() {
        return "sign_in";
    }

    @GetMapping("/sign_up")
    public String sign_up() {
        return "sign_up";
    }

    @PostMapping("/sign_up_action")
    public String signUp(User user) {
        userService.signUp(user);
        return "redirect:/sign_in";
    }

    @PostMapping("/sign_in_action")
    public String signIn(UserLoginDTO user, HttpServletRequest request) {
        if (userService.signIn(user)) {
            // Uzyskaj lub utwórz sesję
            HttpSession session = request.getSession(true);
            // Zapisz SecurityContext w sesji pod kluczem SPRING_SECURITY_CONTEXT
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            System.out.println("Sign OK i zapisano w sesji");
            return "redirect:/";
        } else {
            return "redirect:/sign_in";
        }
    }
}
