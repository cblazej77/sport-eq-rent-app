package com.example.SportsProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReservationAddDTO {

    private Long equipmentID;

    @NotNull(message = "{error.required.quantity}")
    private Integer quantity;

    @NotNull(message = "{error.required.pickupDate}")
    private LocalDate pickupDate;

    @NotNull(message = "{error.required.returnDate}")
    private LocalDate returnDate;

    @NotNull(message = "{error.required.email}")
    private String email;
}
