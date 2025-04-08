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
    @Column(name = "reservation-id")
    private Long reservationID;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "reservation-date", nullable = false)
    private String reservationDate;

    @Column(name = "pickup-date", nullable = false)
    private String pickupDate;

    @Column(name = "return-date", nullable = false)
    private String returnDate;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipmentID", nullable = false)
    private Equipment equipment;

}
