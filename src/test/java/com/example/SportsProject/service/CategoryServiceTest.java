package com.example.SportsProject.service;

import com.example.SportsProject.dto.CategoryAddDTO;
import com.example.SportsProject.dto.CategoryEditDTO;
import com.example.SportsProject.dto.CategoryWithQuantityDTO;
import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAllCategories() {
        Category category = new Category();
        category.setCategoryID(1L);
        category.setName("Fitness");
        category.setImage(new byte[]{1, 2, 3});

        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryRepository.sumEquipmentQuantityByCategoryId(1L)).thenReturn(5L);

        List<CategoryWithQuantityDTO> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Fitness", result.get(0).getName());
        assertEquals(5L, result.get(0).getQuantity());
    }

    @Test
    void categoryAdd() throws IOException {
        CategoryAddDTO dto = new CategoryAddDTO();
        dto.setName("Added");

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getBytes()).thenReturn(new byte[]{10, 20});
        dto.setImage(mockFile);

        Category savedCategory = new Category();
        savedCategory.setName("Added");
        savedCategory.setImage(new byte[]{10, 20});

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        Category result = categoryService.categoryAdd(dto);

        assertEquals("Added", result.getName());
        assertArrayEquals(new byte[]{10, 20}, result.getImage());
    }

    @Test
    void categoryEdit() throws IOException {
        CategoryEditDTO dto = new CategoryEditDTO();
        dto.setCategoryID(1L);
        dto.setName("Edited");

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getBytes()).thenReturn(new byte[]{1, 2, 3});
        dto.setImage(mockFile);

        Category category = new Category();
        category.setCategoryID(1L);
        category.setName("Old");

        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(categoryRepository.save(any())).thenReturn(category);

        Category result = categoryService.categoryEdit(dto);

        assertEquals("Edited", result.getName());
        assertArrayEquals(new byte[]{1, 2, 3}, result.getImage());
    }

    @Test
    void categoryDelete() {
        Equipment eq1 = new Equipment();
        eq1.setEquipmentID(100L);
        Equipment eq2 = new Equipment();
        eq2.setEquipmentID(200L);

        Category category = new Category();
        category.setCategoryID(1L);
        category.setEquipmentList(List.of(eq1, eq2));

        when(categoryRepository.getReferenceById(1L)).thenReturn(category);

        categoryService.categoryDelete(1L);

        verify(equipmentService).equipmentDelete(100L);
        verify(equipmentService).equipmentDelete(200L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void getCategoryById() {
        Category category = new Category();
        category.setCategoryID(1L);
        category.setName("Fitness");

        when(categoryRepository.findCategoryByCategoryID(1L)).thenReturn(category);

        Category result = categoryService.getCategoryById(1L);

        assertEquals("Fitness", result.getName());
    }
}