package org.snowzen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.TaskPO;
import org.snowzen.repository.dao.TaskRepository;
import org.snowzen.repository.dao.relation.CategoryTaskRelationRepository;
import org.snowzen.repository.dao.relation.TagTaskRelationRepository;
import org.snowzen.review.ReviewStrategy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author snow-zen
 */
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MockServletContext.class)
@SpringBootTest
public class TaskServiceTest {

    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TagTaskRelationRepository tagTaskRelationRepository;

    @Mock
    private CategoryTaskRelationRepository categoryTaskRelationRepository;

    @BeforeEach
    public void createService() {
        taskService = new TaskService(taskRepository, tagTaskRelationRepository, categoryTaskRelationRepository);
    }

    @Test
    public void testAddTaskWithoutOther() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("测试任务");
        taskDTO.setContent("测试任务内容");
        taskDTO.setFinishTime(LocalDateTime.now());
        taskDTO.setReviewStrategy(ReviewStrategy.EVERY_DAY);

        when(taskRepository.save(any(TaskPO.class))).then(invocation -> {
            TaskPO taskPO = invocation.getArgument(0, TaskPO.class);
            taskPO.prePersistCallback();
            taskPO.setId(1);
            return taskPO;
        });

        taskService.addTask(taskDTO);
    }

    @Test
    public void testAddTask() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("测试分类");

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("测试标签");

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("测试任务");
        taskDTO.setContent("测试任务内容");
        taskDTO.setFinishTime(LocalDateTime.now());
        taskDTO.setReviewStrategy(ReviewStrategy.EVERY_DAY);
        taskDTO.setCategories(Collections.singletonList(categoryDTO));
        taskDTO.setTags(Collections.singletonList(tagDTO));

        when(taskRepository.save(any(TaskPO.class))).then(invocation -> {
            TaskPO taskPO = invocation.getArgument(0, TaskPO.class);
            taskPO.prePersistCallback();
            taskPO.setId(1);
            return taskPO;
        });
        when(tagTaskRelationRepository.saveAll(any())).then(invocation -> invocation.getArgument(0, List.class));
        when(categoryTaskRelationRepository.saveAll(any())).then(invocation -> invocation.getArgument(0, List.class));

        taskService.addTask(taskDTO);
    }
}
