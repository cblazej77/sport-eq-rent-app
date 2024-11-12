package com.example.SportsProject.entities;

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
    @Column(name = "categoryID")
    private Long categoryID;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<EquipmentType> equipmentTypeList;
}
