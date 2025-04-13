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
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category-id")
    private Long categoryID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image")
    private byte[] image;

    @OneToMany(mappedBy = "category")
    private List<Equipment> equipmentList;
}
