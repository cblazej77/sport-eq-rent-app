package com.example.SportsProject.repository;

import com.example.SportsProject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.categoryID = :categoryID")
    Category findCategoryByCategoryID(@Param("categoryID") Long categoryID);

    @Query("SELECT SUM(e.quantity) FROM Equipment e WHERE e.category.categoryID = :categoryId")
    Long sumEquipmentQuantityByCategoryId(@Param("categoryId") Long categoryId);
}
