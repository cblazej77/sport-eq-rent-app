package com.example.SportsProject.service;

import com.example.SportsProject.dto.EquipmentAddDTO;
import com.example.SportsProject.dto.EquipmentEditDTO;
import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.EquipmentRepository;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public void equipmentDelete(Long equipmentID) {
        equipmentRepository.deleteById(equipmentID);
    }

    public List<Equipment> getEquipmentByCategoryWithFilters(Long categoryID, String sortType, String filterName, Float priceMin, Float priceMax, Boolean available) {
        List<Equipment> equipmentList = equipmentRepository.findByCategory_CategoryID(categoryID);

        if (filterName != null) {
            LevenshteinDistance levenshtein = new LevenshteinDistance();
            equipmentList = equipmentList.stream()
                    .filter(e -> {
                        String name = e.getName().toLowerCase().replaceAll("\\s+", "");
                        String term = filterName.toLowerCase().replaceAll("\\s+", "");
                        int distance = levenshtein.apply(name, term);
                        return distance <= 2 || name.contains(term);
                    })
                    .toList();
        }

        if (priceMin != null) {
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getPrice() >= priceMin)
                    .toList();
        }

        if (priceMax != null) {
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getPrice() <= priceMax)
                    .toList();
        }

        if (available == null) {
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getQuantity() >= 0)
                    .toList();
        } else if (available) {
            equipmentList = equipmentList.stream()
                    .filter(e -> e.getQuantity() > 0)
                    .toList();
        }

        switch (sortType) {
            case "priceLH":
                equipmentList = equipmentList.stream()
                        .sorted(Comparator.comparingDouble(Equipment::getPrice))
                        .collect(Collectors.toList());
                break;
            case "priceHL":
                equipmentList = equipmentList.stream()
                        .sorted(Comparator.comparingDouble(Equipment::getPrice).reversed())
                        .collect(Collectors.toList());
                break;
            case "quantityLH":
                equipmentList = equipmentList.stream()
                        .sorted(Comparator.comparingInt(Equipment::getQuantity))
                        .collect(Collectors.toList());
                break;
            case "quantityHL":
                equipmentList = equipmentList.stream()
                        .sorted(Comparator.comparingInt(Equipment::getQuantity).reversed())
                        .collect(Collectors.toList());
                break;
            default:
                break;
        }

        return equipmentList;
    }
}
