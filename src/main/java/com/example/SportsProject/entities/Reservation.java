package com.example.SportsProject.entities;

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

    @Column(name = "status")
    private ReservationStatus reservationStatus;

    @Column(name = "startingDate")
    private String startingDate;

    @Column(name = "endingDate")
    private String endingDate;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipmentID")
    private Equipment equipment;

}
