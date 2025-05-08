package com.example.SportsProject.dto;

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
public class CategoryEditDTO {
    private Long categoryID;

    @NotBlank(message = "{error.required.name}")
    @Size(min = 2, max = 50, message = "{error.size.name}")
    private String name;

    @NotNull(message = "{error.required.image}")
    private MultipartFile image;
}
