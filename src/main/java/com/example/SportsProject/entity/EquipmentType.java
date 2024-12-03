package com.example.SportsProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "EquipmentType")
public class EquipmentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipmentTypeID")
    private Long equipmentTypeID;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private float price;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "count")
    private int count;

    @ManyToOne
    @JoinColumn(name = "categoryID")
    private Category category;
}
