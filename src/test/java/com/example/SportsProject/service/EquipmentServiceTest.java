package com.example.SportsProject.service;

import com.example.SportsProject.dto.EquipmentAddDTO;
import com.example.SportsProject.dto.EquipmentEditDTO;
import com.example.SportsProject.entity.Category;
import com.example.SportsProject.entity.Equipment;
import com.example.SportsProject.repository.CategoryRepository;
import com.example.SportsProject.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private EquipmentService equipmentService;

    @Test
    void getAllEquipmentTypes_returnsList() {
        List<Equipment> equipmentList = List.of(new Equipment(), new Equipment());
        when(equipmentRepository.findAll()).thenReturn(equipmentList);

        List<Equipment> result = equipmentService.getAllEquipmentTypes();

        assertEquals(2, result.size());
        verify(equipmentRepository).findAll();
    }

    @Test
    void equipmentTypeAdd_savesAndReturnsEquipment() {
        EquipmentAddDTO dto = new EquipmentAddDTO();
        dto.setName("Ball");
        dto.setPrice(20.0f);
        dto.setCategoryID(1L);

        Category category = new Category();
        when(categoryRepository.findCategoryByCategoryID(1L)).thenReturn(category);

        Equipment saved = new Equipment();
        when(equipmentRepository.save(any())).thenReturn(saved);

        Equipment result = equipmentService.equipmentTypeAdd(dto);

        assertNotNull(result);
        verify(equipmentRepository).save(any());
    }

    @Test
    void equipmentAdd_addsFullEquipment() throws IOException {
        EquipmentAddDTO dto = new EquipmentAddDTO();
        dto.setName("Ball");
        dto.setPrice(20.0f);
        dto.setDescription("A football");
        dto.setQuantity(5);
        dto.setImage(new MockMultipartFile("image", "img.jpg", "image/jpeg", new byte[]{1, 2, 3}));
        dto.setCategoryID(1L);

        Category category = new Category();
        when(categoryRepository.findCategoryByCategoryID(1L)).thenReturn(category);
        when(equipmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Equipment result = equipmentService.equipmentAdd(dto);

        assertEquals("Ball", result.getName());
        assertEquals(5, result.getQuantity());
        assertArrayEquals(new byte[]{1, 2, 3}, result.getImage());
    }

    @Test
    void equipmentEdit_editsEquipment() throws IOException {
        EquipmentEditDTO dto = new EquipmentEditDTO();
        dto.setEquipmentID(10L);
        dto.setName("Helmet");
        dto.setPrice(99.0f);
        dto.setDescription("For safety");
        dto.setQuantity(2);
        dto.setImage(new MockMultipartFile("image", "helmet.png", "image/png", new byte[]{4, 5}));
        dto.setCategoryID(3L);

        Equipment existing = new Equipment();
        when(equipmentRepository.getEquipmentByEquipmentID(10L)).thenReturn(existing);
        when(categoryRepository.findCategoryByCategoryID(3L)).thenReturn(new Category());
        when(equipmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Equipment edited = equipmentService.equipmentEdit(dto);

        assertNotNull(edited);
        assertEquals("Helmet", edited.getName());
        assertEquals(2, edited.getQuantity());
        assertEquals(99.0f, edited.getPrice());
        assertArrayEquals(new byte[]{4, 5}, edited.getImage());
    }

    @Test
    void equipmentDelete_callsRepositoryDelete() {
        equipmentService.equipmentDelete(99L);
        verify(equipmentRepository).deleteById(99L);
    }

    @Test
    void getEquipmentByCategoryWithFilters_returnsFilteredList() {
        Equipment eq1 = new Equipment();
        eq1.setName("Ball");
        eq1.setPrice(20.0f);
        eq1.setQuantity(5);

        Equipment eq2 = new Equipment();
        eq2.setName("Helmet");
        eq2.setPrice(40.0f);
        eq2.setQuantity(0);

        when(equipmentRepository.findByCategory_CategoryID(1L)).thenReturn(List.of(eq1, eq2));

        List<Equipment> filtered = equipmentService.getEquipmentByCategoryWithFilters(
                1L, "priceLH", "ball", 10f, 30f, true
        );

        assertEquals(1, filtered.size());
        assertEquals("Ball", filtered.get(0).getName());
    }
}
