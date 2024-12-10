package com.example.SportsProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentAddDTO {
    private String name;
    private float price;
    private Long categoryID;
}
