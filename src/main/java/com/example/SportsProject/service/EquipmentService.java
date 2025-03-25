package com.example.SportsProject.service;

import com.example.SportsProject.dto.EquipmentAddDTO;
import com.example.SportsProject.dto.EquipmentEditDTO;
import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final CategoryRepository categoryRepository;

    public EquipmentService(EquipmentRepository equipmentTypeRepository, CategoryRepository categoryRepository) {
        this.equipmentRepository = equipmentTypeRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Equipment> getAllEquipmentTypes() {
        return equipmentRepository.findAll();
    }

    public Equipment equipmentTypeAdd(EquipmentAddDTO equipmentAddDTO) {
        Equipment equipment = new Equipment();

        equipment.setName(equipmentAddDTO.getName());
        equipment.setPrice(equipmentAddDTO.getPrice());
        equipment.setCategory(categoryRepository.findCategoryByCategoryID(equipmentAddDTO.getCategoryID()));

        return equipmentRepository.save(equipment);
    }

    public Equipment equipmentAdd(EquipmentAddDTO equipmentAddDTO) throws IOException {
        Equipment equipment = new Equipment();

        equipment.setName(equipmentAddDTO.getName());
        equipment.setPrice(equipmentAddDTO.getPrice());
        equipment.setDescription(equipmentAddDTO.getDescription());
        equipment.setQuantity(equipmentAddDTO.getQuantity());
        equipment.setImage(equipmentAddDTO.getImage().getBytes());
        equipment.setCategory(categoryRepository.findCategoryByCategoryID(equipmentAddDTO.getCategoryID()));

        return equipmentRepository.save(equipment);
    }

    public Equipment equipmentEdit(EquipmentEditDTO equipmentEditDTO) throws IOException {
        Equipment equipment = equipmentRepository.getEquipmentByEquipmentID(equipmentEditDTO.getEquipmentID());

        equipment.setName(equipmentEditDTO.getName());
        equipment.setPrice(equipmentEditDTO.getPrice());
        equipment.setDescription(equipmentEditDTO.getDescription());
        equipment.setQuantity(equipmentEditDTO.getQuantity());
        equipment.setImage(equipmentEditDTO.getImage().getBytes());
        equipment.setCategory(categoryRepository.findCategoryByCategoryID(equipmentEditDTO.getCategoryID()));

        return equipmentRepository.save(equipment);
    }

    public List<Equipment> getEquipmentByCategory(Long categoryID) {
        return equipmentRepository.findByCategory_CategoryID(categoryID);
    }

//    public EquipmentType equipmentTypeDelete(Long equipmentID)
}
