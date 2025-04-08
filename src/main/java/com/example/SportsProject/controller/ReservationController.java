package com.example.SportsProject.controller;

import com.example.SportsProject.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<String> addReservation(@RequestParam String pickupDate,
                                                 @RequestParam String returnDate,
                                                 @RequestParam String email,
                                                 @RequestParam Long equipmentID) {

        reservationService.addReservation(pickupDate, returnDate, email, equipmentID);

        return ResponseEntity.ok("OK");
    }
}
