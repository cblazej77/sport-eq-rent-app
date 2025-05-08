package com.example.SportsProject.controller;

import com.example.SportsProject.dto.CategoryAddDTO;
import com.example.SportsProject.dto.CategoryEditDTO;
import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.UserRepository;
import com.example.SportsProject.service.CategoryService;
import com.example.SportsProject.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> addCategory(@ModelAttribute @Valid CategoryAddDTO categoryAddDTO,
                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        categoryService.categoryAdd(categoryAddDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editCategory(@ModelAttribute @Valid CategoryEditDTO categoryEditDTO,
                                          BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        categoryService.categoryEdit(categoryEditDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{categoryID}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryID) {
        categoryService.categoryDelete(categoryID);

        return ResponseEntity.ok().build();
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
        model.addAttribute("minPickupDate", LocalDate.now().toString());
        model.addAttribute("maxPickupDate", LocalDate.now().plusWeeks(2).toString());
        model.addAttribute("maxReturnDate", LocalDate.now().plusMonths(2).toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findUserByEmail(userDetails.getUsername());
            model.addAttribute("user", user);
        }

        return "equipment";
    }
}
