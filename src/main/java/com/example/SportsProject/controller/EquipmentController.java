package com.example.SportsProject.controller;

import ch.qos.logback.core.model.Model;
import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.EquipmentType;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.EquipmentTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EquipmentController {
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final CategoryRepository categoryRepository;

    public EquipmentController(EquipmentTypeRepository equipmentTypeRepository, CategoryRepository categoryRepository) {
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/eq_add_endp")
    public ResponseEntity<Map<String, String>> eq_add_endp(@RequestBody EquipmentType equipmentType) {
        Map<String, String> response = new HashMap<>();
        System.out.println("eq_add_endp");
        try {
            equipmentTypeRepository.save(equipmentType);
            response.put("message", "equipment type added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error adding equipment type");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/cat_add")
    public String addCategory(Category category, Model model) {
        categoryRepository.save(category);
        return "redirect:/eq_add";
    }

}
