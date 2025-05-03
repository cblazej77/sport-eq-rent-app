package com.example.SportsProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryWithQuantityDTO {
    private Long categoryID;
    private String name;
    private byte[] image;
    private Long quantity;
}
