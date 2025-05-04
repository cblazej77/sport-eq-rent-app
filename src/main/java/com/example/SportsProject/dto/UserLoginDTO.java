package com.example.SportsProject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginDTO {

    @NotBlank(message = "{error.required.email}")
    private String email;

    @NotBlank(message = "{error.required.password}")
    private String password;

    private Boolean sendEmail;
}
