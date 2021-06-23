package org.snowzen.model.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.TaskPO;
import org.snowzen.review.ReviewStrategy;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author snow-zen
 */
@SuppressWarnings("WrongUsageOfMappersFactory")
public class TaskAssemblerTest {

    private TaskAssembler assembler;

    @BeforeEach
    public void createAssembler() {
        assembler = Mappers.getMapper(TaskAssembler.class);
    }

    @Test
    public void testPO2DTO() {
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

        TaskDTO taskDTO = assembler.PO2DTO(taskPO);

        assertNotNull(taskDTO);
        assertEquals(taskPO.getId(), taskDTO.getId());
        assertEquals(taskPO.getTitle(), taskDTO.getTitle());
        assertEquals(taskPO.getContent(), taskDTO.getContent());
        assertEquals(taskPO.getFinishTime(), taskDTO.getFinishTime());
        assertEquals(taskPO.getActive(), taskDTO.getActive());
        assertEquals(taskPO.getReviewStrategy(), taskDTO.getReviewStrategy());
        assertEquals(taskPO.getCreateTime(), taskDTO.getCreateTime());
        assertEquals(taskPO.getModifiedTime(), taskDTO.getModifiedTime());
        assertNull(taskDTO.getCategories());
        assertNull(taskDTO.getTags());
    }

    @Test
    public void testDTO2PO() {
        LocalDateTime time = LocalDateTime.of(2021, 1, 1, 0, 0);

        CategoryDTO categoryDTO = new CategoryDTO();
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
        taskDTO.setCategories(Collections.singletonList(categoryDTO));
        taskDTO.setTags(Collections.singletonList(tagDTO));

        TaskPO taskPO = assembler.DTO2PO(taskDTO);

        assertNotNull(taskPO);
        assertEquals(taskPO.getId(), taskDTO.getId());
        assertEquals(taskPO.getTitle(), taskDTO.getTitle());
        assertEquals(taskPO.getContent(), taskDTO.getContent());
        assertEquals(taskPO.getFinishTime(), taskDTO.getFinishTime());
        assertEquals(taskPO.getActive(), taskDTO.getActive());
        assertEquals(taskPO.getReviewStrategy(), taskDTO.getReviewStrategy());
        assertEquals(taskPO.getCreateTime(), taskDTO.getCreateTime());
        assertEquals(taskPO.getModifiedTime(), taskDTO.getModifiedTime());
    }

    @Test
    public void testDTO2POWithoutCategoriesAndTag() {
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

        TaskPO taskPO = assembler.DTO2PO(taskDTO);

        assertNotNull(taskPO);
        assertEquals(taskPO.getId(), taskDTO.getId());
        assertEquals(taskPO.getTitle(), taskDTO.getTitle());
        assertEquals(taskPO.getContent(), taskDTO.getContent());
        assertEquals(taskPO.getFinishTime(), taskDTO.getFinishTime());
        assertEquals(taskPO.getActive(), taskDTO.getActive());
        assertEquals(taskPO.getReviewStrategy(), taskDTO.getReviewStrategy());
        assertEquals(taskPO.getCreateTime(), taskDTO.getCreateTime());
        assertEquals(taskPO.getModifiedTime(), taskDTO.getModifiedTime());
    }
}
