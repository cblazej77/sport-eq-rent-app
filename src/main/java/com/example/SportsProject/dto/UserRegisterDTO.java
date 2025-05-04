package com.example.SportsProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterDTO {

    @NotBlank(message = "{error.required.name}")
    @Size(min = 2, max = 50, message = "{error.size.name}")
    String name;

    @NotBlank(message = "{error.required.surname}")
    @Size(min = 2, max = 50, message = "{error.size.surname}")
    String surname;

    @NotBlank(message = "{error.required.email}")
    @Size(min = 2, max = 50, message = "{error.size.email}")
    String email;

    @NotBlank(message = "{error.required.password}")
    @Size(min = 6, max = 20, message = "{error.size.password}")
    String password;
}
