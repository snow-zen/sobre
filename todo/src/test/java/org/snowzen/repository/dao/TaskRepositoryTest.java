package org.snowzen.repository.dao;

import org.junit.jupiter.api.Test;
import org.snowzen.model.po.CategoryPO;
import org.snowzen.model.po.TaskPO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        CategoryPO categoryPO = new CategoryPO();
        categoryPO.setName("测试分类");
        categoryPO = categoryRepository.save(categoryPO);

        Integer categoryId = categoryPO.getId();

        TaskPO taskPO1 = new TaskPO();
        taskPO1.setTitle("测试任务1");
        taskPO1.setContent("测试任务内容");
        taskPO1.setCategoryId(categoryId);
        TaskPO taskPO2 = new TaskPO();
        taskPO2.setTitle("测试任务2");
        taskPO2.setContent("测试任务内容");
        taskPO2.setCategoryId(categoryId);

        List<TaskPO> taskPOList = taskRepository.saveAll(Arrays.asList(taskPO1, taskPO2));
        List<TaskPO> taskPOListFromDB = taskRepository.findAllByCategoryId(categoryId);

        assertEquals(taskPOList, taskPOListFromDB);
    }

    @Test
    public void testFindAllTitleLike() {
        String key = "测试";

        TaskPO taskPO1 = new TaskPO();
        taskPO1.setTitle("测试任务");
        taskPO1.setContent("测试任务内容");

        TaskPO taskPO2 = new TaskPO();
        taskPO2.setTitle("任务");
        taskPO2.setContent("测试任务内容");

        taskRepository.saveAll(Arrays.asList(taskPO1, taskPO2));
        List<TaskPO> taskPOListFromDB = taskRepository.findAllByTitleContaining(key);

        assertNotNull(taskPOListFromDB);
        assertEquals(1, taskPOListFromDB.size());
        assertEquals(taskPOListFromDB.get(0), taskPO1);
    }

    @Test
    public void testFindAllByFinishTimeBefore() {
        LocalDateTime now = LocalDateTime.now();
        TaskPO taskPO1 = new TaskPO();
        taskPO1.setTitle("测试任务1");
        taskPO1.setContent("测试任务内容");
        taskPO1.setFinishTime(now.minusDays(1));

        TaskPO taskPO2 = new TaskPO();
        taskPO2.setTitle("测试任务2");
        taskPO2.setContent("测试任务内容");
        taskPO2.setFinishTime(now.plusDays(1));

        taskRepository.saveAll(Arrays.asList(taskPO1, taskPO2));
        List<TaskPO> taskPOList = taskRepository.findAllByFinishTimeBefore(now);

        assertNotNull(taskPOList);
        assertEquals(1, taskPOList.size());
        assertEquals(taskPOList.get(0), taskPO1);
    }
}
