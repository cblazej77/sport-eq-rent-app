package com.example.SportsProject.service;

import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.entity.Reservation;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.EquipmentRepository;
import com.example.SportsProject.repository.ReservationRepository;
import com.example.SportsProject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public Reservation addReservation(String pickupDate,
                                      String returnDate,
                                      String email,
                                      Long equipmentID) {

        Reservation reservation = new Reservation();
        Equipment equipment = equipmentRepository.getEquipmentByEquipmentID(equipmentID);

        reservation.setStatus("RESERVED");
        reservation.setReservationDate(LocalDateTime.now().toString());
        reservation.setPickupDate(pickupDate);
        reservation.setReturnDate(returnDate);
        reservation.setUser(userRepository.findUserByEmail(email));
        reservation.setEquipment(equipment);

        equipment.setQuantity(equipment.getQuantity() - 1);

        equipmentRepository.save(equipment);
        return reservationRepository.save(reservation);
    }
}
