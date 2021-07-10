package org.snowzen.model.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.TaskPO;
import org.snowzen.review.ReviewStrategy;
import org.snowzen.service.CategoryService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author snow-zen
 */
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MockServletContext.class)
@SpringBootTest
public class TaskAssemblerTest {

    @Mock
    private CategoryService categoryService;

    private TaskAssembler assembler;

    @BeforeEach
    public void createAssembler() {
        assembler = new TaskAssembler();
        ReflectionTestUtils.setField(assembler, "categoryService", categoryService);
    }

    @Test
    public void testToDTO() {
        LocalDateTime time = LocalDateTime.of(2021, 1, 1, 0, 0);

        TaskPO taskPO = new TaskPO();
        taskPO.setId(1);
        taskPO.setTitle("测试任务");
        taskPO.setContent("测试内容");
        taskPO.setFinishTime(time);
        taskPO.setActive(Boolean.TRUE);
        taskPO.setReviewStrategy(ReviewStrategy.EVERY_DAY);
        taskPO.setCreateTime(time);
        taskPO.setModifiedTime(time);
        taskPO.setCategoryId(1);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("electric");

        when(categoryService.findCategoryById(1)).thenReturn(categoryDTO);

        TaskDTO taskDTO = assembler.toDTO(taskPO);

        assertNotNull(taskDTO);
        assertEquals(taskPO.getId(), taskDTO.getId());
        assertEquals(taskPO.getTitle(), taskDTO.getTitle());
        assertEquals(taskPO.getContent(), taskDTO.getContent());
        assertEquals(taskPO.getFinishTime(), taskDTO.getFinishTime());
        assertEquals(taskPO.getActive(), taskDTO.getActive());
        assertEquals(taskPO.getReviewStrategy(), taskDTO.getReviewStrategy());
        assertEquals(taskPO.getCreateTime(), taskDTO.getCreateTime());
        assertEquals(taskPO.getModifiedTime(), taskDTO.getModifiedTime());
        assertEquals(taskPO.getCategoryId(), taskDTO.getCategory().getId());
        assertNull(taskDTO.getTags());
    }

    @Test
    public void testToPO() {
        LocalDateTime time = LocalDateTime.of(2021, 1, 1, 0, 0);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("steer");

        TagDTO tagDTO = new TagDTO();

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1);
        taskDTO.setTitle("测试任务");
        taskDTO.setContent("测试内容");
        taskDTO.setFinishTime(time);
        taskDTO.setActive(Boolean.TRUE);
        taskDTO.setReviewStrategy(ReviewStrategy.EVERY_DAY);
        taskDTO.setCreateTime(time);
        taskDTO.setModifiedTime(time);
        taskDTO.setCategory(categoryDTO);
        taskDTO.setTags(Collections.singletonList(tagDTO));

        TaskPO taskPO = assembler.toPO(taskDTO);

        assertNotNull(taskPO);
        assertEquals(taskPO.getId(), taskDTO.getId());
        assertEquals(taskPO.getTitle(), taskDTO.getTitle());
        assertEquals(taskPO.getContent(), taskDTO.getContent());
        assertEquals(taskPO.getFinishTime(), taskDTO.getFinishTime());
        assertEquals(taskPO.getActive(), taskDTO.getActive());
        assertEquals(taskPO.getReviewStrategy(), taskDTO.getReviewStrategy());
        assertEquals(taskPO.getCreateTime(), taskDTO.getCreateTime());
        assertEquals(taskPO.getModifiedTime(), taskDTO.getModifiedTime());
        assertEquals(taskPO.getCategoryId(), taskDTO.getCategory().getId());
    }

    @Test
    public void testToPOWithoutCategoriesAndTag() {
        LocalDateTime time = LocalDateTime.of(2021, 1, 1, 0, 0);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1);
        taskDTO.setTitle("测试任务");
        taskDTO.setContent("测试内容");
        taskDTO.setFinishTime(time);
        taskDTO.setActive(Boolean.TRUE);
        taskDTO.setReviewStrategy(ReviewStrategy.EVERY_DAY);
        taskDTO.setCreateTime(time);
        taskDTO.setModifiedTime(time);

        TaskPO taskPO = assembler.toPO(taskDTO);

        assertNotNull(taskPO);
        assertEquals(taskPO.getId(), taskDTO.getId());
        assertEquals(taskPO.getTitle(), taskDTO.getTitle());
        assertEquals(taskPO.getContent(), taskDTO.getContent());
        assertEquals(taskPO.getFinishTime(), taskDTO.getFinishTime());
        assertEquals(taskPO.getActive(), taskDTO.getActive());
        assertEquals(taskPO.getReviewStrategy(), taskDTO.getReviewStrategy());
        assertEquals(taskPO.getCreateTime(), taskDTO.getCreateTime());
        assertEquals(taskPO.getModifiedTime(), taskDTO.getModifiedTime());
        assertNull(taskPO.getCategoryId());
    }
}
