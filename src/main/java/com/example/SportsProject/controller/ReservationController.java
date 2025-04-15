package com.example.SportsProject.controller;

import com.example.SportsProject.entity.Reservation;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.UserRepository;
import com.example.SportsProject.service.ReservationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        }
        return "reservations";
    }

    @PostMapping("/new")
    @ResponseBody
    public String addReservation(@RequestParam int quantity,
                                 @RequestParam String pickupDate,
                                 @RequestParam String returnDate,
                                 @RequestParam String email,
                                 @RequestParam Long equipmentID) {

        reservationService.addReservation(quantity, pickupDate, returnDate, email, equipmentID);

        return "reservations";
    }

    @PutMapping("/change-status")
    public String changeStatus(@RequestParam Long reservationID,
                               @RequestParam String status) {

        reservationService.changeStatus(reservationID, status);

        return "redirect:/reservations";
    }

}
