package com.example.SportsProject.service;

import com.example.SportsProject.dto.ReservationAddDTO;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.entity.Reservation;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.EquipmentRepository;
import com.example.SportsProject.repository.ReservationRepository;
import com.example.SportsProject.repository.UserRepository;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {

    @Autowired
    private final JavaMailSender mailSender;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    public ReservationService(JavaMailSender mailSender, ReservationRepository reservationRepository,
                              UserRepository userRepository, EquipmentRepository equipmentRepository) {
        this.mailSender = mailSender;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public List<Reservation> getReservationList(User user, String filterText) {
        List<Reservation> reservationList;

        if (Objects.equals(user.getRole(), "ADMIN")) {
            reservationList = reservationRepository.findAll();
        } else {
            reservationList = reservationRepository.findAllByUser(user);
        }

        if (filterText != null) {
            LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
            reservationList = reservationList.stream()
                    .filter(e -> {
                        String name = e.getReservationCode().toLowerCase().replaceAll("\\s+", "");
                        String term = filterText.toLowerCase().replaceAll("\\s+", "");
                        int distance = levenshteinDistance.apply(name, term);
                        return distance <= 2 || name.contains(term);
                    })
                    .toList();
        }

        for (Reservation reservation : reservationList) {
            reservation.setReservationDate(LocalDateTime.parse(reservation.getReservationDate())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        return reservationList;
    }

    public Reservation addReservation(ReservationAddDTO reservationAddDTO) {

        Reservation reservation = new Reservation();
        Equipment equipment = equipmentRepository.getEquipmentByEquipmentID(reservationAddDTO.getEquipmentID());

        String resCodeDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String resCodeID = String.format("%04d", reservationRepository.count() % 10000);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.now();

        String pickupDate = reservationAddDTO.getPickupDate().toString();
        String returnDate = reservationAddDTO.getReturnDate().toString();

        int daysInUse = Period.between(LocalDate.parse(pickupDate), LocalDate.parse(returnDate)).getDays();

        reservation.setStatus("RESERVED");
        reservation.setReservationCode("RES-" + resCodeDate + "-" + resCodeID);
        reservation.setReservationDate(localDateTime.toString());
        reservation.setQuantity(reservation.getQuantity());
        reservation.setPickupDate(pickupDate);
        reservation.setReturnDate(returnDate);
        reservation.setUser(userRepository.findUserByEmail(reservationAddDTO.getEmail()));
        reservation.setEquipment(equipment);
        reservation.setCost(reservation.getQuantity() * equipment.getPrice() * (daysInUse));

        equipment.setQuantity(equipment.getQuantity() - 1);

        String subject = "SportEqRent - Your reservation";
        String message = "Equipment has been reserved on " + localDateTime.format(formatter) +
                ". Here is your reservation code: " + reservation.getReservationCode() +
                ". Please show it at the store to verify your reservation. Make sure to pick up your equipment before " +
                reservation.getPickupDate() + ".\n\n" + "Equipment details:\n" + equipment.getName() + "\n" +
                equipment.getDescription();

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(reservation.getUser().getEmail());
        mail.setSubject(subject);
        mail.setText(message);

        mailSender.send(mail);

        equipmentRepository.save(equipment);
        return reservationRepository.save(reservation);
    }

    public Reservation changeStatus(Long reservationID, String status) {
        Reservation reservation = reservationRepository.getReferenceById(reservationID);

        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }
}
