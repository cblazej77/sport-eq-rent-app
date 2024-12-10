package com.example.SportsProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipmentID")
    private Long equipmentID;

    @ManyToOne
    @JoinColumn(name = "equipmentTypeID", nullable = false)
    private EquipmentType equipmentType;

    @OneToMany(mappedBy = "equipment")
    private List<Reservation> reservationList;
}
