package com.example.SportsProject.controller;

import ch.qos.logback.core.model.Model;
import com.example.SportsProject.dto.EquipmentAddDTO;
import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.EquipmentType;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.EquipmentTypeRepository;
import com.example.SportsProject.service.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EquipmentController {
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final EquipmentService equipmentService;
    private final CategoryRepository categoryRepository;

    public EquipmentController(EquipmentTypeRepository equipmentTypeRepository, EquipmentService equipmentService, CategoryRepository categoryRepository) {
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.equipmentService = equipmentService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/equipment_type_add")
    public String equipmentTypeAdd(EquipmentAddDTO equipmentAddDTO, Model model) {
        equipmentService.equipmentTypeAdd(equipmentAddDTO);
        return "equipment_types";
    }

    @GetMapping("/equipment_types")
    public String equipmentTypes() {
        return "equipment_types";
    }
}
