package com.example.SportsProject.controller;

import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.EquipmentRepository;
import com.example.SportsProject.service.EquipmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquipmentController.class)
class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipmentService equipmentService;

    @MockBean
    private EquipmentRepository equipmentRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @WithMockUser(roles = "ADMIN")
    @Test
    void equipmentAdd() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "fake".getBytes());

        mockMvc.perform(multipart("/equipment_add")
                        .file(imageFile)
                        .param("name", "Test Equipment")
                        .param("description", "Some description")
                        .param("price", "100")
                        .param("categoryID", "1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void equipmentEdit() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "fake".getBytes());

        mockMvc.perform(multipart("/equipment_edit")
                        .file(imageFile)
                        .param("equipmentID", "1")
                        .param("name", "Edited Equipment")
                        .param("description", "Edited desc")
                        .param("price", "150")
                        .param("categoryID", "2")
                        .with(csrf())
                        .with(request -> {
                            request.setMethod("PUT"); // multipart domyślnie używa POST — trzeba wymusić PUT
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void equipmentDelete() throws Exception {
        Equipment mockEquipment = new Equipment();
        Category mockCategory = new Category();
        mockCategory.setCategoryID(1L);
        mockEquipment.setCategory(mockCategory);

        when(equipmentRepository.getEquipmentByEquipmentID(1L)).thenReturn(mockEquipment);

        mockMvc.perform(delete("/equipment_delete/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    void getImage() throws Exception {
        Equipment equipment = new Equipment();
        equipment.setImage("test image".getBytes());

        when(equipmentRepository.getEquipmentByEquipmentID(1L)).thenReturn(equipment);

        mockMvc.perform(get("/equipment/image/1"))
                .andExpect(status().isOk())
                .andExpect(content().bytes("test image".getBytes()));
    }
}
