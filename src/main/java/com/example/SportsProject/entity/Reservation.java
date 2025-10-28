package com.example.SportsProject.entity;

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

    @Column(name = "reservation-code", nullable = false)
    private String reservationCode;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "cost", nullable = false)
    private float cost;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "reservation-date", nullable = false)
    private String reservationDate;

    @Column(name = "pickup-date", nullable = false)
    private String pickupDate;

    @Column(name = "return-date", nullable = false)
    private String returnDate;

    @ManyToOne
    @JoinColumn(name = "user-id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipment-id", nullable = false)
    private Equipment equipment;

}
