package com.example.SportsProject.controller;

import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.UserRepository;
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
import java.util.Objects;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final EquipmentService equipmentService;
    private final UserRepository userRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository, EquipmentService equipmentService, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.equipmentService = equipmentService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findUserByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        return "categories";
    }

    @GetMapping("/image/{categoryID}")
    @ResponseBody
    public byte[] getImage(@PathVariable Long categoryID) {
        Category category = categoryService.getCategoryById(categoryID);
        return category.getImage();
    }

    @PostMapping("/add")
    public String addCategory(@RequestParam String name, @RequestParam MultipartFile image) {
        categoryService.categoryAdd(name, image);
        return "redirect:/categories";
    }

    @PutMapping("/edit")
    public String editCategory(@RequestParam Long categoryID, @RequestParam String name, @RequestParam MultipartFile image) {
        categoryService.categoryEdit(categoryID, name, image);
        return "redirect:/categories";
    }

    @DeleteMapping("/delete/{categoryID}")
    public String deleteCategory(@PathVariable Long categoryID) {
        categoryService.categoryDelete(categoryID);
        return  "redirect:/categories";
    }

    @GetMapping("/{categoryID}")
    public String getEquipmentByCategory(
            @PathVariable Long categoryID,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) String filterName,
            @RequestParam(required = false) Float filterPriceMin,
            @RequestParam(required = false) Float filterPriceMax,
            @RequestParam(required = false) Boolean filterAvailable,
            Model model) {
        if (sortType == null) {sortType = "default";}
        System.out.println("searchName: " + filterName);
        Category category = categoryService.getCategoryById(categoryID);
        System.out.println(filterAvailable);
        List<Equipment> equipment = equipmentService.getEquipmentByCategoryWithFilters(categoryID, sortType, filterName, filterPriceMin, filterPriceMax, filterAvailable);

        model.addAttribute("sortType", sortType);
        model.addAttribute("filterName", Objects.requireNonNullElse(filterName, ""));
        model.addAttribute("filterPriceMin", Objects.requireNonNullElse(filterPriceMin, ""));
        model.addAttribute("filterPriceMax", Objects.requireNonNullElse(filterPriceMax, ""));
        model.addAttribute("filterAvailable", filterAvailable);
        model.addAttribute("categoryID", categoryID);
        model.addAttribute("categoryName", category.getName());
        model.addAttribute("equipmentList", equipment);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findUserByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        return "equipment";
    }
}
