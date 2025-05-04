package com.example.SportsProject.controller;

import com.example.SportsProject.dto.UserLoginDTO;
import com.example.SportsProject.dto.UserRegisterDTO;
import com.example.SportsProject.repository.UserRepository;
import com.example.SportsProject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class UserController {
    @Autowired
    private MessageSource messageSource;
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(MessageSource messageSource, UserService userService, UserRepository userRepository) {
        this.messageSource = messageSource;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/sign_in")
    public String showLoginForm(Model model,
                                @RequestParam(value = "message", required = false) String message) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        model.addAttribute("message", message);
        return "sign_in";
    }

    @GetMapping("/sign_up")
    public String showRegisterForm(Model model) {
        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "sign_up";
    }

    @PostMapping("/sign_up_action")
    public String signUp(@Valid UserRegisterDTO userRegisterDTO,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "sign_up";
        } else if (userService.signUp(userRegisterDTO)) {
            return "redirect:/sign_in?message=registered";
        } else {
            Locale locale = LocaleContextHolder.getLocale();
            model.addAttribute("registerError", messageSource.getMessage("error.sign_up", null, locale));
            return "sign_up";
        }
    }

    @PostMapping("/sign_in_action")
    public String signIn(@Valid UserLoginDTO user,
                         BindingResult bindingResult,
                         HttpServletRequest request,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "sign_in";
        }
        String signInOutput = userService.signIn(user);
        Locale locale = LocaleContextHolder.getLocale();
        switch (signInOutput) {
            case "OK" -> {
                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                return "redirect:/";
            }
            case "SEND_TOKEN" -> {
                model.addAttribute("loginError", messageSource.getMessage("error.sign_in.sendToken", null, locale));
                return "sign_in";
            }
            case "NOT_VERIFIED" -> {
                model.addAttribute("loginError", messageSource.getMessage("error.sign_in.unverified", null, locale));
                return "sign_in";
            }
            case "INCORRECT_INPUT" -> {
                model.addAttribute("loginError", messageSource.getMessage("error.sign_in.input", null, locale));
                return "sign_in";
            }
            default -> {
                model.addAttribute("loginError", messageSource.getMessage("error.unknown", null, locale));
                return "sign_in";
            }
        }
    }

    @GetMapping("/get_name/{username}")
    public String getName(@PathVariable String username) {
        return userRepository.findUserByEmail(username).getName();
    }
}
