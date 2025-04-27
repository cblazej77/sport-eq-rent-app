package com.example.SportsProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review-id")
    private Long reviewID;

    @Column(name = "text")
    private String text;

    @Column(name = "rate", nullable = false)
    private int rate;

    @Column(name = "date")
    private String date;

    @ManyToOne
    @JoinColumn(name = "user-id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipment-id", nullable = false)
    private Equipment equipment;

}
