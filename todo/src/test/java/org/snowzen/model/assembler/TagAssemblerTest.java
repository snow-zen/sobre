package org.snowzen.model.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.TagPO;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author snow-zen
 */
@SuppressWarnings("WrongUsageOfMappersFactory")
public class TagAssemblerTest {

    private TagAssembler assembler;

    @BeforeEach
    public void createAssembler() {
        assembler = Mappers.getMapper(TagAssembler.class);
    }

    @Test
    public void testPO2DTO() {
        TagPO tagPO = new TagPO();
        tagPO.setId(1);
        tagPO.setName("测试标签");

        TagDTO tagDTO = assembler.PO2DTO(tagPO);

        assertNotNull(tagDTO);
        assertEquals(tagPO.getId(), tagDTO.getId());
        assertEquals(tagPO.getName(), tagDTO.getName());
        assertNull(tagDTO.getTasks());
    }

    @Test
    public void testDTO2PO() {
        TaskDTO taskDTO = new TaskDTO();

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("测试标签");
        tagDTO.setTasks(Collections.singletonList(taskDTO));

        TagPO tagPO = assembler.DTO2PO(tagDTO);

        assertNotNull(tagPO);
        assertEquals(tagDTO.getId(), tagPO.getId());
        assertEquals(tagDTO.getName(), tagPO.getName());
    }

    @Test
    public void testDTO2POWithoutTask() {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("测试标签");

        TagPO tagPO = assembler.DTO2PO(tagDTO);

        assertNotNull(tagPO);
        assertEquals(tagDTO.getId(), tagPO.getId());
        assertEquals(tagDTO.getName(), tagPO.getName());
    }
}
