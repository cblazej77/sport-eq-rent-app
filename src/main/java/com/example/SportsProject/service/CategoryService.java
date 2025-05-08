package com.example.SportsProject.service;

import com.example.SportsProject.dto.CategoryAddDTO;
import com.example.SportsProject.dto.CategoryEditDTO;
import com.example.SportsProject.dto.CategoryWithQuantityDTO;
import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EquipmentService equipmentService;

    public CategoryService(CategoryRepository categoryRepository, EquipmentService equipmentService) {
        this.categoryRepository = categoryRepository;
        this.equipmentService = equipmentService;
    }

    public List<CategoryWithQuantityDTO> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryWithQuantityDTO> categoryWithQuantityDTOS = new ArrayList<>();

        for(Category category : categoryList) {
            CategoryWithQuantityDTO categoryWithQuantityDTO = new CategoryWithQuantityDTO();
            categoryWithQuantityDTO.setCategoryID(category.getCategoryID());
            categoryWithQuantityDTO.setImage(category.getImage());
            categoryWithQuantityDTO.setName(category.getName());

            Long quantity = categoryRepository.sumEquipmentQuantityByCategoryId(category.getCategoryID());
            categoryWithQuantityDTO.setQuantity(quantity == null ? 0 : quantity);

            categoryWithQuantityDTOS.add(categoryWithQuantityDTO);
        }

        return categoryWithQuantityDTOS;
    }

    public Category categoryAdd(CategoryAddDTO categoryAddDTO) {
        Category category = new Category();
        category.setName(categoryAddDTO.getName());
        MultipartFile image = categoryAddDTO.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                category.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Błąd podczas zapisu obrazu.", e);
            }
        }
        return categoryRepository.save(category);
    }

    public Category categoryEdit(CategoryEditDTO categoryEditDTO) {
        Category category = categoryRepository.getReferenceById(categoryEditDTO.getCategoryID());

        category.setName(categoryEditDTO.getName());
        MultipartFile image = categoryEditDTO.getImage();
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
