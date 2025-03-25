package com.example.SportsProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentEditDTO {
    private Long equipmentID;
    private String name;
    private String description;
    private float price;
    private int quantity;
    private MultipartFile image;
    private Long categoryID;
}
