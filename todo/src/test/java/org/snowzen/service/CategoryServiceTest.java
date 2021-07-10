package org.snowzen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.snowzen.exception.NotFoundDataException;
import org.snowzen.model.assembler.CategoryAssembler;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.po.CategoryPO;
import org.snowzen.repository.dao.CategoryRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author snow-zen
 */
@SuppressWarnings("WrongUsageOfMappersFactory")
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MockServletContext.class)
@SpringBootTest
public class CategoryServiceTest {

    private CategoryService categoryService;

    private CategoryAssembler categoryAssembler;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void before() {
        categoryAssembler = Mappers.getMapper(CategoryAssembler.class);
        categoryService = new CategoryService(categoryRepository, categoryAssembler);
    }

    @Test
    public void testFindCategoryById() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("测试分类");

        CategoryPO categoryPO = categoryAssembler.toPO(categoryDTO);

        when(categoryRepository.findById(1))
                .thenReturn(Optional.of(categoryPO));

        assertEquals(categoryDTO, categoryService.findCategoryById(1));
    }

    @Test
    public void testFindCategoryByIdNotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundDataException.class, () -> categoryService.findCategoryById(1));
    }

    @Test
    public void testAddCategory() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("测试分类");

        when(categoryRepository.save(any(CategoryPO.class)))
                .then(invocation -> invocation.getArgument(0, CategoryPO.class));

        assertDoesNotThrow(() -> categoryService.addCategory(categoryDTO));
    }

    @Test
    public void testHasCategory() {
        when(categoryRepository.countAllByIdIn(any())).thenReturn(1);

        assertDoesNotThrow(() -> assertTrue(categoryService.hasCategory(1)));
    }

    @Test
    public void testEnsureCategory() {
        when(categoryRepository.countAllByIdIn(any())).thenReturn(1);

        assertDoesNotThrow(() -> categoryService.ensureCategory(1));
    }

    @Test
    public void testEnsureCategoryThrowException() {
        when(categoryRepository.countAllByIdIn(any())).thenReturn(0);

        assertThrows(NotFoundDataException.class, () -> categoryService.ensureCategory(1));
    }
}
