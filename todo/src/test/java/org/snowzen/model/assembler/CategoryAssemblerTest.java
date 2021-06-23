package org.snowzen.model.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.CategoryPO;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author snow-zen
 */
@SuppressWarnings("WrongUsageOfMappersFactory")
public class CategoryAssemblerTest {

    private CategoryAssembler assembler;

    @BeforeEach
    public void createAssemble() {
        assembler = Mappers.getMapper(CategoryAssembler.class);
    }

    @Test
    public void testNotNull() {
        assertNotNull(assembler);
    }

    @Test
    public void testPO2DTO() {
        CategoryPO categoryPO = new CategoryPO();
        categoryPO.setId(1);
        categoryPO.setName("测试分类");

        CategoryDTO categoryDTO = assembler.PO2DTO(categoryPO);

        assertNotNull(categoryDTO);
        assertEquals(categoryPO.getId(), categoryDTO.getId());
        assertEquals(categoryPO.getName(), categoryDTO.getName());
        assertNull(categoryDTO.getTasks());
    }

    @Test
    public void testDTO2PO() {
        TaskDTO taskDTO = new TaskDTO();

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("测试分类");
        categoryDTO.setTasks(Collections.singletonList(taskDTO));

        CategoryPO categoryPO = assembler.DTO2PO(categoryDTO);

        assertNotNull(categoryPO);
        assertEquals(categoryDTO.getId(), categoryPO.getId());
        assertEquals(categoryDTO.getName(), categoryPO.getName());
    }

    @Test
    public void testDTO2POWithoutTaskDTO() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("测试分类");

        CategoryPO categoryPO = assembler.DTO2PO(categoryDTO);

        assertNotNull(categoryPO);
        assertEquals(categoryDTO.getId(), categoryPO.getId());
        assertEquals(categoryDTO.getName(), categoryPO.getName());
    }
}
