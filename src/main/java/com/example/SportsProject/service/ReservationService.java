package com.example.SportsProject.service;

import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.entity.Reservation;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.EquipmentRepository;
import com.example.SportsProject.repository.ReservationRepository;
import com.example.SportsProject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, EquipmentRepository equipmentRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public List<Reservation> getReservationList(User user) {
        List<Reservation> reservationList;

        if (Objects.equals(user.getRole(), "ADMIN")) {
            reservationList = reservationRepository.findAll();
        } else {
            reservationList = reservationRepository.findAllByUser(user);
        }

        return reservationList;
    }

    public Reservation addReservation(int quantity,
                                      String pickupDate,
                                      String returnDate,
                                      String email,
                                      Long equipmentID) {

        Reservation reservation = new Reservation();
        Equipment equipment = equipmentRepository.getEquipmentByEquipmentID(equipmentID);

        String resCodeDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String resCodeID = String.format("%04d", reservationRepository.count() % 10000);

        reservation.setStatus("RESERVED");
        reservation.setReservationCode("RES-" + resCodeDate + "-" + resCodeID);
        reservation.setReservationDate(LocalDateTime.now().toString());
        reservation.setQuantity(quantity);
        reservation.setPickupDate(pickupDate);
        reservation.setReturnDate(returnDate);
        reservation.setUser(userRepository.findUserByEmail(email));
        reservation.setEquipment(equipment);

        equipment.setQuantity(equipment.getQuantity() - 1);

        equipmentRepository.save(equipment);
        return reservationRepository.save(reservation);
    }

    public Reservation changeStatus(Long reservationID, String status) {
        Reservation reservation = reservationRepository.getReferenceById(reservationID);

        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }
}
