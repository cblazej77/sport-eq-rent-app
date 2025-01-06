package com.example.SportsProject.controller;

import com.example.SportsProject.entity.Category;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    @PostMapping("/category_add")
    public String addCategory(String name) {
        categoryService.categoryAdd(name);
        return "redirect:/categories";
    }
}
