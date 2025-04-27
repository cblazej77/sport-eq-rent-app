package com.example.SportsProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "EmailVerification")
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email-verification-id")
    private Long emailVerificationID;

    @Column(name = "token")
    private String token;

    @Column(name = "expiry-date")
    private String expiryDate;

    @ManyToOne
    @JoinColumn(name = "user-id")
    private User user;
}
