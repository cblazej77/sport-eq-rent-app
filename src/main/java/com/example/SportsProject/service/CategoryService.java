package com.example.SportsProject.service;

import com.example.SportsProject.entity.Category;
import com.example.SportsProject.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category categoryAdd(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
}
