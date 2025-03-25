package com.example.SportsProject.controller;

import ch.qos.logback.core.model.Model;
import com.example.SportsProject.dto.EquipmentAddDTO;
import com.example.SportsProject.dto.EquipmentEditDTO;
import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.EquipmentRepository;
import com.example.SportsProject.service.EquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Controller
public class EquipmentController {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentService equipmentService;
    private final CategoryRepository categoryRepository;

    public EquipmentController(EquipmentRepository equipmentTypeRepository, EquipmentService equipmentService, CategoryRepository categoryRepository) {
        this.equipmentRepository = equipmentTypeRepository;
        this.equipmentService = equipmentService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/equipment_add")
    public String equipmentAdd(EquipmentAddDTO equipmentAddDTO) throws IOException {
        equipmentService.equipmentAdd(equipmentAddDTO);
        return "redirect:/categories";
    }

    @PutMapping("/equipment_edit")
    public String equipmentEdit(@ModelAttribute EquipmentEditDTO equipmentEditDTO) throws IOException {
        System.out.println(equipmentEditDTO.getEquipmentID());
        System.out.println(equipmentEditDTO.getCategoryID());
        System.out.println(equipmentEditDTO.getName());
        System.out.println(equipmentEditDTO.getDescription());
        System.out.println(equipmentEditDTO.getPrice());
        equipmentService.equipmentEdit(equipmentEditDTO);
        return "redirect:/categories";
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

    @GetMapping("/equipment/image/{equipmentID}")
    @ResponseBody
    public byte[] getImage(@PathVariable Long equipmentID) {
        Equipment equipment = equipmentRepository.getEquipmentByEquipmentID(equipmentID);
        return equipment.getImage();
    }
}
