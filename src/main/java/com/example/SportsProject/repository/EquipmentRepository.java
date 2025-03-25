package com.example.SportsProject.repository;

import com.example.SportsProject.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByCategory_CategoryID(Long categoryID);

    Equipment getEquipmentByEquipmentID(Long equipmentID);
}
