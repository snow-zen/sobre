package org.snowzen.model;

import org.junit.jupiter.api.Test;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.CategoryPO;
import org.snowzen.model.po.TagPO;
import org.snowzen.model.po.TaskPO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author snow-zen
 */
public class ModelConversionTest {

    @Test
    public void testCategoryConversion() {
        CategoryPO prototype = new CategoryPO();
        prototype.setId(1);
        prototype.setName("测试分类");

        CategoryDTO dto = prototype.convert();

        assertEquals(dto.getId(), prototype.getId());
        assertEquals(dto.getName(), prototype.getName());

        CategoryPO copy = new CategoryPO();
        copy.reverse(dto);

        assertEquals(copy, prototype);
    }

    @Test
    public void testTagConversion() {
        TagPO prototype = new TagPO();
        prototype.setId(1);
        prototype.setName("测试分类");

        TagDTO dto = prototype.convert();

        assertEquals(dto.getId(), prototype.getId());
        assertEquals(dto.getName(), prototype.getName());

        TagPO copy = new TagPO();
        copy.reverse(dto);

        assertEquals(copy, prototype);
    }

    @Test
    public void testTaskConversion() {
        TaskPO prototype = new TaskPO();
        prototype.setId(1);
        prototype.setTitle("测试任务");
        prototype.setContent("测试任务内容");
        prototype.setFinishTime(LocalDateTime.now());
        prototype.prePersistCallback();

        TaskDTO dto = prototype.convert();

        assertEquals(dto.getId(), prototype.getId());
        assertEquals(dto.getTitle(), prototype.getTitle());
        assertEquals(dto.getContent(), prototype.getContent());
        assertEquals(dto.getFinishTime(), prototype.getFinishTime());
        assertEquals(dto.getActive(), prototype.getActive());
        assertEquals(dto.getCreateTime(), prototype.getCreateTime());
        assertEquals(dto.getModifiedTime(), prototype.getModifiedTime());

        TaskPO copy = new TaskPO();
        copy.reverse(dto);

        assertEquals(copy, prototype);
    }
}
