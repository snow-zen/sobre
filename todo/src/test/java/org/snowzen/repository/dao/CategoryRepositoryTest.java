package org.snowzen.repository.dao;

import org.junit.jupiter.api.Test;
import org.snowzen.model.po.CategoryPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author snow-zen
 */
@Transactional
@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

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
