package com.example.SportsProject.controller;

import com.example.SportsProject.entity.Category;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.service.EquipmentService;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public String index(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(authentication);
//        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
//            System.out.println("Authentication OK");
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            model.addAttribute("user", userDetails);
//            System.out.println(userDetails);
//        } else {
//            System.out.println("NO Auth");
//        }

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
