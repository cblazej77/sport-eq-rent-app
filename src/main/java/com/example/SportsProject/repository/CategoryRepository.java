package com.example.SportsProject.repository;

import com.example.SportsProject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByCategoryID(Long categoryID);
}
