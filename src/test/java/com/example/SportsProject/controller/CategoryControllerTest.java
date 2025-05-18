package com.example.SportsProject.controller;

import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.UserRepository;
import com.example.SportsProject.service.CategoryService;
import com.example.SportsProject.service.EquipmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private EquipmentService equipmentService;

    @WithMockUser(username = "test@example.com", roles = {"USER"})
    @Test
    void categoriesWithUser() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of());
        when(userRepository.findUserByEmail(anyString())).thenReturn(new User());

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("user"));
    }

    @WithMockUser(username = "test@example.com", roles = {"USER"})
    @Test
    void getImage() throws Exception {
        byte[] imageBytes = new byte[]{1, 2, 3};
        Category category = new Category();
        category.setImage(imageBytes);

        when(categoryService.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(get("/categories/image/1"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(imageBytes));
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void addCategory() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        mockMvc.perform(multipart("/categories/add")
                        .file(imageFile)
                        .param("name", "Test category")
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(categoryService).categoryAdd(any());
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void editCategory() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        mockMvc.perform(multipart("/categories/edit")
                        .file(imageFile)
                        .param("categoryID", "1")
                        .param("name", "Category 1")
                        .with(csrf())
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        verify(categoryService).categoryEdit(any());
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void deleteCategory_shouldReturnOk_whenCategoryExists() throws Exception {
        mockMvc.perform(delete("/categories/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(categoryService).categoryDelete(1L);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void getEquipmentByCategory() throws Exception {
        Category category = new Category();
        category.setName("Category1");

        Equipment equipment = new Equipment();
        equipment.setEquipmentID(1L);
        equipment.setName("Equip1");
        equipment.setPrice(100f);

        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(equipmentService.getEquipmentByCategoryWithFilters(eq(1L), anyString(), any(), any(), any(), any()))
                .thenReturn(List.of(equipment));

        User user = new User();
        user.setUserID(1L);
        user.setEmail("user@example.com");
        user.setRole("USER");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(user.getEmail());
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(user);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipment"))
                .andExpect(model().attributeExists("equipmentList"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("categoryName"));
    }
}
