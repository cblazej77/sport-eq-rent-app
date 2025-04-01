package com.example.SportsProject.service;

import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EquipmentService equipmentService;

    public CategoryService(CategoryRepository categoryRepository, EquipmentService equipmentService) {
        this.categoryRepository = categoryRepository;
        this.equipmentService = equipmentService;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category categoryAdd(String name, MultipartFile image) {
        Category category = new Category();
        category.setName(name);
        if (image != null && !image.isEmpty()) {
            try {
                category.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Błąd podczas zapisu obrazu.", e);
            }
        }
        return categoryRepository.save(category);
    }

    public Category categoryEdit(Long categoryID, String name, MultipartFile image) {
        Category category = categoryRepository.getReferenceById(categoryID);

        category.setName(name);
        if (image != null && !image.isEmpty()) {
            try {
                category.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Błąd podczas zapisu obrazu.", e);
            }
        }

        return categoryRepository.save(category);
    }

    public void categoryDelete(Long categoryID) {
        Category category = categoryRepository.getReferenceById(categoryID);
        List<Equipment> equipmentList = category.getEquipmentList();

        for (Equipment equipment : equipmentList) {
            equipmentService.equipmentDelete(equipment.getEquipmentID());
        }

        categoryRepository.deleteById(categoryID);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findCategoryByCategoryID(id);
    }
}
