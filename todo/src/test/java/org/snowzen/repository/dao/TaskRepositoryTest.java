package org.snowzen.repository.dao;

import org.junit.jupiter.api.Test;
import org.snowzen.model.po.CategoryPO;
import org.snowzen.model.po.TaskPO;
import org.snowzen.model.po.relation.CategoryTaskRelationPO;
import org.snowzen.repository.dao.relation.CategoryTaskRelationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author snow-zen
 */
@Transactional
@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryTaskRelationRepository categoryTaskRelationRepository;

    @Test
    public void testDuplicateTask() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            TaskPO taskPO1 = new TaskPO();
            taskPO1.setTitle("测试任务");
            taskPO1.setContent("测试任务内容");

            TaskPO taskPO2 = new TaskPO();
            BeanUtils.copyProperties(taskPO1, taskPO2);

            taskRepository.save(taskPO1);
            taskRepository.save(taskPO2);
        });
    }

    @Test
    public void testFindAllByCategoryId() {
        TaskPO taskPO1 = new TaskPO();
        taskPO1.setTitle("测试任务1");
        taskPO1.setContent("测试任务内容");
        TaskPO taskPO2 = new TaskPO();
        taskPO2.setTitle("测试任务2");
        taskPO2.setContent("测试任务内容");

        CategoryPO categoryPO = new CategoryPO();
        categoryPO.setName("测试分类");

        List<TaskPO> taskPOList = taskRepository.saveAll(Arrays.asList(taskPO1, taskPO2));
        categoryPO = categoryRepository.save(categoryPO);

        Integer categoryId = categoryPO.getId();
        List<CategoryTaskRelationPO> categoryTaskRelationPOList = taskPOList.stream().map(taskPO -> {
            CategoryTaskRelationPO categoryTaskRelationPO = new CategoryTaskRelationPO();
            categoryTaskRelationPO.setTaskId(taskPO.getId());
            categoryTaskRelationPO.setCategoryId(categoryId);
            return categoryTaskRelationPO;
        }).collect(Collectors.toList());

        categoryTaskRelationRepository.saveAll(categoryTaskRelationPOList);

        List<TaskPO> taskPOListFromDB = taskRepository.findAllByCategoryId(categoryId);
        assertEquals(taskPOList, taskPOListFromDB);
    }
}
