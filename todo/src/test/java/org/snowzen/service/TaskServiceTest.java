package org.snowzen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.snowzen.exception.NotFoundDataException;
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
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Test
    public void testFindTask() {
        TaskPO taskPO = new TaskPO();
        taskPO.setId(1);
        taskPO.setTitle("测试任务");
        taskPO.setContent("测试任务内容");
        taskPO.setFinishTime(LocalDateTime.now());
        taskPO.setReviewStrategy(ReviewStrategy.EVERY_DAY);
        taskPO.setCreateTime(LocalDateTime.now());
        taskPO.setModifiedTime(LocalDateTime.now());

        when(taskRepository.findById(1)).thenReturn(Optional.of(taskPO));

        TaskDTO taskDTO = taskService.findTask(1);
        assertEquals(taskPO.convert(), taskDTO);
    }

    @Test
    public void testFindTaskThrowException() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundDataException.class, () -> taskService.findTask(1));
    }

    @Test
    public void testFindAllByCategoryId() {
        TaskPO taskPO = new TaskPO();
        taskPO.setId(1);
        taskPO.setTitle("测试任务");
        taskPO.setContent("测试任务内容");
        taskPO.setFinishTime(LocalDateTime.now());
        taskPO.setReviewStrategy(ReviewStrategy.EVERY_DAY);
        taskPO.setCreateTime(LocalDateTime.now());
        taskPO.setModifiedTime(LocalDateTime.now());

        when(taskRepository.findAllByCategoryId(1)).thenReturn(Collections.singletonList(taskPO));

        List<TaskDTO> taskDTOList = taskService.findAllByCategoryId(1);

        assertFalse(CollectionUtils.isEmpty(taskDTOList));
        assertEquals(1, taskDTOList.size());
        assertEquals(taskPO.convert(), taskDTOList.get(0));
    }

    @Test
    public void testFindAllByKey() {
        String key = "测试";

        TaskPO taskPO1 = new TaskPO();
        taskPO1.setId(1);
        taskPO1.setTitle("测试任务");
        taskPO1.setContent("测试内容");

        TaskPO taskPO2 = new TaskPO();
        taskPO2.setId(2);
        taskPO2.setTitle("任务");
        taskPO2.setContent("测试内容");

        when(taskRepository.findAllByTitleContaining(key)).thenReturn(Collections.singletonList(taskPO1));

        List<TaskDTO> taskDTOList = taskService.findAllByKey(key);

        assertFalse(CollectionUtils.isEmpty(taskDTOList));
        assertEquals(1, taskDTOList.size());
        assertEquals(taskPO1.convert(), taskDTOList.get(0));
    }

    @Test
    public void testFindAllByKeyUsingEmptyKey() {
        String key = "";

        TaskPO taskPO1 = new TaskPO();
        taskPO1.setId(1);
        taskPO1.setTitle("测试任务");
        taskPO1.setContent("测试内容");

        TaskPO taskPO2 = new TaskPO();
        taskPO2.setId(2);
        taskPO2.setTitle("任务");
        taskPO2.setContent("测试内容");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(taskPO1, taskPO2));

        List<TaskDTO> taskDTOList = taskService.findAllByKey(key);

        assertFalse(CollectionUtils.isEmpty(taskDTOList));
        assertEquals(2, taskDTOList.size());
        assertEquals(Stream.of(taskPO1, taskPO2).map(TaskPO::convert).collect(Collectors.toList()), taskDTOList);
    }

    @Test
    public void testFindAllNeedReview() {
        TaskPO taskPO1 = new TaskPO();
        taskPO1.setId(1);
        taskPO1.setTitle("测试任务");
        taskPO1.setContent("测试内容");
        taskPO1.setFinishTime(LocalDateTime.of(2021, 1, 1, 0, 0));

        when(taskRepository.findAllByFinishTimeBefore(any())).thenReturn(Collections.singletonList(taskPO1));

        List<TaskDTO> taskDTOList = taskService.findAllNeedReview();

        assertFalse(CollectionUtils.isEmpty(taskDTOList));
        assertEquals(1, taskDTOList.size());
        assertEquals(taskPO1.convert(), taskDTOList.get(0));
    }
}
