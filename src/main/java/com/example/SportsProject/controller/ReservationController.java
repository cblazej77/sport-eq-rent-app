package com.example.SportsProject.controller;

import com.example.SportsProject.dto.ReservationAddDTO;
import com.example.SportsProject.entity.Reservation;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.UserRepository;
import com.example.SportsProject.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserRepository userRepository;

    public ReservationController(ReservationService reservationService, UserRepository userRepository) {
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showReservations(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findUserByEmail(userDetails.getUsername());
            List<Reservation> reservationList = reservationService.getReservationList(user);
            model.addAttribute("user", user);
            model.addAttribute("reservationList", reservationList);
            model.addAttribute("today", LocalDate.now());
            System.out.println(LocalDate.now());
        }
        return "reservations";
    }

    @PostMapping("/new")
    public ResponseEntity<?> addReservation(@ModelAttribute @Valid ReservationAddDTO reservationAddDTO,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        reservationService.addReservation(reservationAddDTO);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-status")
    public String changeStatus(@RequestParam Long reservationID,
                               @RequestParam String status) {

        reservationService.changeStatus(reservationID, status);

        return "redirect:/reservations";
    }

}
