package com.example.SportsProject.controller;

import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.service.CategoryService;
import com.example.SportsProject.service.EquipmentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLOutput;
import java.util.List;

@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final EquipmentService equipmentService;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository, EquipmentService equipmentService) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.equipmentService = equipmentService;
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("user", userDetails);
        }

        return "categories";
    }

    @GetMapping("/category/image/{categoryID}")
    @ResponseBody
    public byte[] getImage(@PathVariable Long categoryID) {
        Category category = categoryService.getCategoryById(categoryID);
        return category.getImage();
    }

    @PostMapping("/category_add")
    public String addCategory(@RequestParam String name, @RequestParam MultipartFile image) {
        categoryService.categoryAdd(name, image);
        return "redirect:/categories";
    }

    @PutMapping("/category_edit")
    public String editCategory(@RequestParam Long categoryID, @RequestParam String name, @RequestParam MultipartFile image) {
        categoryService.categoryEdit(categoryID, name, image);
        return "redirect:/categories";
    }

//    @PostMapping("/category_delete")
//    public String deleteCategory(@RequestParam Long categoryID) {
//        categoryService.categoryDelete(categoryID);
//        return  "redirect:/categories";
//    }

    @GetMapping("/categories/{name}")
    public String openCategory(@PathVariable String name) {
        return "redirect:/categories/{name}";
    }

    @GetMapping("/category/{categoryID}/equipment")
    public String getEquipmentByCategory(@PathVariable Long categoryID, Model model) {
        // Pobieranie kategorii
        Category category = categoryService.getCategoryById(categoryID);

        // Pobieranie wyposażenia dla danej kategorii
        List<Equipment> equipment = equipmentService.getEquipmentByCategory(categoryID);

        // Dodanie danych do modelu
        model.addAttribute("categoryID", categoryID);
        model.addAttribute("categoryName", category.getName());
        model.addAttribute("equipmentList", equipment);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("user", userDetails);
        }

        return "equipment";
    }
}
