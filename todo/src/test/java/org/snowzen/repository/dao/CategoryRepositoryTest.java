package org.snowzen.repository.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.snowzen.model.po.CategoryPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author snow-zen
 */
@Transactional
@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryPO categoryPO;

    @BeforeEach
    public void before() {
        categoryPO = new CategoryPO();
        categoryPO.setName("测试分类");
    }

    @Test
    public void testFindTaskByCategory() {
        CategoryPO category = categoryRepository.save(categoryPO);

        assertTrue(CollectionUtils.isEmpty(category.getTasks()));
    }

    @Test
    public void testDuplicateCategory() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            CategoryPO categoryPO1 = new CategoryPO();
            categoryPO1.setName("测试分类");

            CategoryPO categoryPO2 = new CategoryPO();
            categoryPO2.setName("测试分类");

            categoryRepository.save(categoryPO1);
            categoryRepository.save(categoryPO2);
        });
    }

}
