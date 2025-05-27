package com.example.SportsProject.service;

import com.example.SportsProject.dto.ReservationAddDTO;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.entity.Reservation;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.EquipmentRepository;
import com.example.SportsProject.repository.ReservationRepository;
import com.example.SportsProject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void addReservation_shouldSaveReservationAndSendEmail() {
        ReservationAddDTO dto = new ReservationAddDTO();
        dto.setEmail("user@example.com");
        dto.setEquipmentID(1L);
        dto.setPickupDate(LocalDate.now());
        dto.setReturnDate(LocalDate.now().plusDays(3));
        dto.setQuantity(2);

        Equipment equipment = new Equipment();
        equipment.setEquipmentID(1L);
        equipment.setPrice(100f);
        equipment.setName("Ski");
        equipment.setDescription("Mountain ski");
        equipment.setQuantity(5);

        User user = new User();
        user.setEmail("user@example.com");

        Reservation saved = new Reservation();

        when(equipmentRepository.getEquipmentByEquipmentID(1L)).thenReturn(equipment);
        when(userRepository.findUserByEmail("user@example.com")).thenReturn(user);
        when(reservationRepository.count()).thenReturn(5L);
        when(reservationRepository.save(any())).thenReturn(saved);

        Reservation result = reservationService.addReservation(dto);

        assertNotNull(result);
        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(equipmentRepository).save(any(Equipment.class));
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void changeStatus_shouldUpdateReservationStatus() {
        Reservation reservation = new Reservation();
        reservation.setStatus("RESERVED");

        when(reservationRepository.getReferenceById(1L)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.changeStatus(1L, "COLLECTED");

        assertEquals("COLLECTED", result.getStatus());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void getReservationList_shouldReturnFilteredListForAdmin() {
        User admin = new User();
        admin.setRole("ADMIN");

        Reservation reservation1 = new Reservation();
        reservation1.setReservationCode("RES-202405-0001");
        reservation1.setReservationDate(LocalDateTime.now().toString());

        Reservation reservation2 = new Reservation();
        reservation2.setReservationCode("OTHER-202405-0002");
        reservation2.setReservationDate(LocalDateTime.now().toString());

        List<Reservation> all = List.of(reservation1, reservation2);

        when(reservationRepository.findAll()).thenReturn(all);

        List<Reservation> result = reservationService.getReservationList(admin, "RES");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getReservationCode().contains("RES"));
    }

    @Test
    void getReservationList_shouldReturnUserReservations() {
        User user = new User();
        user.setRole("USER");

        Reservation reservation = new Reservation();
        reservation.setReservationCode("RES-202405-0003");
        reservation.setReservationDate(LocalDateTime.now().toString());

        when(reservationRepository.findAllByUser(user)).thenReturn(List.of(reservation));

        List<Reservation> result = reservationService.getReservationList(user, null);

        assertEquals(1, result.size());
        assertEquals("RES-202405-0003", result.get(0).getReservationCode());
    }
}
