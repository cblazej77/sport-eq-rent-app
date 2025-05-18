package com.example.SportsProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentDTO {
    private Long equipmentID;
    private String name;
    private String description;
    private float price;
    private byte[] image;
    private int quantity;

    public EquipmentDTO(Long equipmentID, String name, String description, float price, byte[] image, int quantity) {
        this.equipmentID = equipmentID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }
}
