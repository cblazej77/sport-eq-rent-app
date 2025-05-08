package com.example.SportsProject.controller;

import ch.qos.logback.core.model.Model;
import com.example.SportsProject.dto.EquipmentAddDTO;
import com.example.SportsProject.dto.EquipmentEditDTO;
import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.EquipmentRepository;
import com.example.SportsProject.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

//    @PostMapping("/equipment_add")
//    public String equipmentAdd(@Valid EquipmentAddDTO equipmentAddDTO, BindingResult bindingResult) throws IOException {
//        equipmentService.equipmentAdd(equipmentAddDTO);
//        return "redirect:/categories/" + equipmentAddDTO.getCategoryID();
//    }

    @PostMapping("/equipment_add")
    public ResponseEntity<?> equipmentAdd(
            @ModelAttribute @Valid EquipmentAddDTO equipmentAddDTO,
            BindingResult result) throws IOException {

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        equipmentService.equipmentAdd(equipmentAddDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/equipment_edit")
    public ResponseEntity<?> equipmentEdit(@ModelAttribute @Valid EquipmentEditDTO equipmentEditDTO,
                                BindingResult result) throws IOException {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        equipmentService.equipmentEdit(equipmentEditDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/equipment_delete/{equipmentID}")
    public ResponseEntity<?> equipmentDelete(@PathVariable Long equipmentID){
        Long categoryID = equipmentRepository.getEquipmentByEquipmentID(equipmentID).getCategory().getCategoryID();
        equipmentService.equipmentDelete(equipmentID);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/equipment/image/{equipmentID}")
    @ResponseBody
    public byte[] getImage(@PathVariable Long equipmentID) {
        Equipment equipment = equipmentRepository.getEquipmentByEquipmentID(equipmentID);
        return equipment.getImage();
    }
}
