package org.snowzen.repository.dao;

import org.junit.jupiter.api.Test;
import org.snowzen.model.po.TaskPO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author snow-zen
 */
@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

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
}
