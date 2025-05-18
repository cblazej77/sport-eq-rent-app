package com.example.SportsProject.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO {
    private Long reservationID;
    private String reservationCode;
    private int quantity;
    private float cost;
    private String status;
    private String reservationDate;
    private String pickupDate;
    private String returnDate;
    private UserDTO user;

    public ReservationDTO(Long reservationID, String reservationCode, int quantity, float cost, String status,
                          String reservationDate, String pickupDate, String returnDate, UserDTO user) {
        this.reservationID = reservationID;
        this.reservationCode = reservationCode;
        this.quantity = quantity;
        this.cost = cost;
        this.status = status;
        this.reservationDate = reservationDate;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.user = user;
    }
}
