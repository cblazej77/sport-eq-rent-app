package com.example.SportsProject.controller;

import com.example.SportsProject.entity.Category;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.service.EquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {


    private final EquipmentService equipmentService;
    private final CategoryRepository categoryRepository;

    public MainController(EquipmentService equipmentService, CategoryRepository categoryRepository) {
        this.equipmentService = equipmentService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/categories";
    }

//    @GetMapping("/home")
//    public String home() {
//        return "home";
//    }

    @GetMapping("/home")
    public String homePage(Model model) {
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categories", categoryList);
        model.addAttribute("equipmentTypes", equipmentService.getAllEquipmentTypes());
        return "home";
    }

}
