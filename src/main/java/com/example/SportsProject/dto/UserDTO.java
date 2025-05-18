package com.example.SportsProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long userID;
    private String email;
    private String role;
    private String name;
    private String surname;

    public UserDTO(Long userID, String email, String role, String name, String surname) {
        this.userID = userID;
        this.email = email;
        this.role = role;
        this.name = name;
        this.surname = surname;
    }
}
