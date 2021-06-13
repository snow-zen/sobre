package org.snowzen.repository.dao;

import org.junit.jupiter.api.Test;
import org.snowzen.model.po.TagPO;
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
@SpringBootTest
@Transactional
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void testFindTaskByTask() {
        TagPO tagPO = new TagPO();
        tagPO.setName("测试标签");
        tagPO = tagRepository.save(tagPO);

        assertTrue(CollectionUtils.isEmpty(tagPO.getTasks()));
    }

    @Test
    public void testDuplicateCategory() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            TagPO tagPO1 = new TagPO();
            tagPO1.setName("测试标签");

            TagPO tagPO2 = new TagPO();
            tagPO2.setName("测试标签");

            tagRepository.save(tagPO1);
            tagRepository.save(tagPO2);
        });
    }
}
