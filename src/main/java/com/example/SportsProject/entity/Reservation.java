package com.example.SportsProject.entity;

import com.example.SportsProject.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservationID")
    private Long reservationID;

    @Column(name = "status", nullable = false)
    private ReservationStatus reservationStatus;

    // Data utworzenia rezerwacji
    @Column(name = "reservationDate", nullable = false)
    private String reservationDate;

    // Data, kiedy klient odbiera sprzęt
    @Column(name = "pickup_date", nullable = false)
    private String pickup_date;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipmentID", nullable = false)
    private Equipment equipment;

}
