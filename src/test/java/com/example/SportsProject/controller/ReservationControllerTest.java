package com.example.SportsProject.controller;

import com.example.SportsProject.dto.ReservationAddDTO;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.UserRepository;
import com.example.SportsProject.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private UserRepository userRepository;

    @WithMockUser(roles = "USER", username = "user@example.com")
    @Test
    void showReservations() throws Exception {
        User user = new User();
        user.setName("Jan");
        user.setSurname("Kowalski");
        user.setEmail("user@example.com");

        when(userRepository.findUserByEmail("user@example.com")).thenReturn(user);

        when(reservationService.getReservationList(user, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservations"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void addReservation() throws Exception {
        mockMvc.perform(post("/reservations/new")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("equipmentID", "1")
                        .param("pickupDate", "2025-06-01")
                        .param("returnDate", "2025-06-02")
                        .param("quantity", "1")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void changeStatus() throws Exception {
        mockMvc.perform(put("/reservations/change-status")
                        .with(csrf())
                        .param("reservationID", "1")
                        .param("status", "APPROVED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations"));
    }
}
