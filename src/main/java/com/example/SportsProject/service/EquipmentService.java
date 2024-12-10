package com.example.SportsProject.service;

import com.example.SportsProject.dto.EquipmentAddDTO;
import com.example.SportsProject.entity.EquipmentType;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.EquipmentTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final CategoryRepository categoryRepository;

    public EquipmentService(EquipmentTypeRepository equipmentTypeRepository, CategoryRepository categoryRepository) {
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<EquipmentType> getAllEquipmentTypes() {
        return equipmentTypeRepository.findAll();
    }

    public EquipmentType equipmentTypeAdd(EquipmentAddDTO equipmentAddDTO) {
        EquipmentType equipmentType = new EquipmentType();

        equipmentType.setName(equipmentAddDTO.getName());
        equipmentType.setPrice(equipmentAddDTO.getPrice());
        equipmentType.setCategory(categoryRepository.findCategoryByCategoryID(equipmentAddDTO.getCategoryID()));

        return equipmentTypeRepository.save(equipmentType);
    }
}
