package com.example.SportsProject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentEditDTO {

    @NotNull(message = "{error.required.equipmentID}")
    private Long equipmentID;

    @NotBlank(message = "{error.required.name}")
    @Size(min = 2, max = 50, message = "{error.size.name}")
    private String name;

    @NotBlank(message = "{error.required.description}")
    @Size(min = 10, max = 255, message = "{error.size.description}")
    private String description;

    @NotNull(message = "{error.required.price}")
    @Min(value = 1, message = "{error.min.price}")
    private float price;

    @NotNull(message = "{error.required.quantity}")
    @Min(value = 0, message = "{error.min.quantity}")
    private int quantity;

    @NotNull(message = "{error.required.image}")
    private MultipartFile image;

    @NotNull(message = "{error.required.categoryID}")
    private Long categoryID;
}
