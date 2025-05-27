package com.example.SportsProject.controller;

import com.example.SportsProject.dto.ReservationAddDTO;
import com.example.SportsProject.dto.ReservationDTO;
import com.example.SportsProject.dto.UserDTO;
import com.example.SportsProject.entity.Reservation;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.EquipmentRepository;
import com.example.SportsProject.repository.UserRepository;
import com.example.SportsProject.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    @Autowired
    private MessageSource messageSource;


    public ReservationController(ReservationService reservationService, UserRepository userRepository, EquipmentRepository equipmentRepository) {
        this.reservationService = reservationService;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @GetMapping
    public String showReservations(Model model, @RequestParam(required = false) String filterText) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findUserByEmail(userDetails.getUsername());
            List<Reservation> reservationList = reservationService.getReservationList(user, filterText);

            List<ReservationDTO> reservationDTOs = reservationList.stream().map(res -> {
                ReservationDTO dto = new ReservationDTO();
                dto.setReservationID(res.getReservationID());
                dto.setReservationCode(res.getReservationCode());
                dto.setReservationDate(res.getReservationDate());
                dto.setPickupDate(res.getPickupDate());
                dto.setReturnDate(res.getReturnDate());
                dto.setStatus(res.getStatus());
                dto.setQuantity(res.getQuantity());
                dto.setCost(res.getCost());

                User u = res.getUser();
                UserDTO userDTO = new UserDTO();
                userDTO.setName(u.getName());
                userDTO.setSurname(u.getSurname());
                userDTO.setEmail(u.getEmail());
                userDTO.setRole(u.getRole());

                dto.setUser(userDTO);
                return dto;
            }).collect(Collectors.toList());

            Collections.reverse(reservationDTOs);

            UserDTO userDTO = new UserDTO();
            userDTO.setName(user.getName());
            userDTO.setSurname(user.getSurname());
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole());

            model.addAttribute("user", userDTO);
            model.addAttribute("reservationList", reservationDTOs);
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("filterText", filterText);
        }
        return "reservations";
    }

    @PostMapping("/new")
    public ResponseEntity<?> addReservation(@ModelAttribute @Valid ReservationAddDTO reservationAddDTO,
                                            BindingResult bindingResult) {

        int maxQuantity = equipmentRepository.getEquipmentByEquipmentID(reservationAddDTO.getEquipmentID()).getQuantity();
        if (bindingResult.hasErrors() || reservationAddDTO.getQuantity() > maxQuantity) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());

            if (reservationAddDTO.getQuantity() > maxQuantity) {
                errors.add(messageSource.getMessage("error.quantity", null, LocaleContextHolder.getLocale()));
            }

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
