package com.example.SportsProject.controller;

import com.example.SportsProject.dto.UserLoginDTO;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.service.UserService;
import org.springframework.stereotype.Controller;
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
    public String signIn(UserLoginDTO user) {
        if (userService.signIn(user)) {
            return "redirect:/home";
        }
        else {
            return "redirect:/sign_in";
        }
    }
}
