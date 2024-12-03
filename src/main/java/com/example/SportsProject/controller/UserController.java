package com.example.SportsProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UserController {

    @GetMapping("/sign_in")
    public String sign_in() {
        return "sign_in";
    }

    @GetMapping("/sign_up")
    public String sign_up() {
        return "sign_up";
    }
}
