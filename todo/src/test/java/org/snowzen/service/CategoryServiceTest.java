package org.snowzen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.snowzen.exception.NotFoundDataException;
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
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MockServletContext.class)
@SpringBootTest
public class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void before() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    public void testFindCategoryById() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("测试分类");

        CategoryPO categoryPO = new CategoryPO();
        categoryPO.reverse(categoryDTO);

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
        when(categoryRepository.existsById(1)).thenReturn(Boolean.TRUE);

        assertDoesNotThrow(() -> assertTrue(categoryService.hasCategory(1)));
    }

    @Test
    public void testEnsureCategory() {
        when(categoryRepository.existsById(1)).thenReturn(Boolean.TRUE);

        assertDoesNotThrow(() -> categoryService.ensureCategory(1));
    }

    @Test
    public void testEnsureCategoryThrowException() {
        when(categoryRepository.existsById(1)).thenReturn(Boolean.FALSE);

        assertThrows(NotFoundDataException.class, () -> categoryService.ensureCategory(1));
    }
}
