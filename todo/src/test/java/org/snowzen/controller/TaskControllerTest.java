package org.snowzen.controller;

import org.junit.jupiter.api.Test;
import org.snowzen.model.assembler.TaskAssembler;
import org.snowzen.service.CategoryService;
import org.snowzen.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author snow-zen
 */
@WebMvcTest(
        value = TaskController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TaskAssembler.class))
public class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private TaskService taskService;

    @Test
    public void testFindTaskUsingInvalidId() throws Exception {
        int negativeTaskId = -1;
        long tooLongTaskId = Long.MAX_VALUE;
        String missTypeTaskId = "4eT9fyd5";

        mvc.perform(get("/task/" + negativeTaskId))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/task/" + tooLongTaskId))
                .andExpect(status().isBadRequest());

        mvc.perform(get("/task/" + missTypeTaskId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddTaskUsingInvalidArgument() throws Exception {
        // 测试缺失title
        //language=JSON
        String withoutTitle = "{\n" +
                "  \"content\": \"post\",\n" +
                "  \"active\": true,\n" +
                "  \"reviewStrategy\": \"EVERY_DAY\",\n" +
                "  \"category\": " +
                "{\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"rather\"\n" +
                "  }" +
                ",\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"wage\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mvc.perform(
                post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withoutTitle)
        ).andExpect(status().isBadRequest());

        // 测试无效reviewStrategy
        //language=JSON
        String illegalReviewStrategy = "{\n" +
                "  \"title\": \"multiply\",\n" +
                "  \"content\": \"hide\",\n" +
                "  \"active\": true,\n" +
                "  \"reviewStrategy\": \"strike\",\n" +
                "  \"category\": " +
                "{\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"wreck\"\n" +
                "  }" +
                ",\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"log\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mvc.perform(
                post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(illegalReviewStrategy)
        ).andExpect(status().isBadRequest());

        // 测试残缺的分类信息
        //language=JSON
        String withIncompleteCategories = "{\n" +
                "  \"title\": \"she\",\n" +
                "  \"content\": \"mile\",\n" +
                "  \"active\": true,\n" +
                "  \"reviewStrategy\": \"EVERY_DAY\",\n" +
                "  \"category\": " +
                "{\n" +
                "      \"name\": \"breath\"\n" +
                "  }" +
                ",\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"beneath\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mvc.perform(
                post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withIncompleteCategories)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateTaskUsingInvalidArgument() throws Exception {
        // 测试缺失id

        //language=JSON
        String withoutId = "{\n" +
                "  \"title\": \"south\",\n" +
                "  \"active\": true,\n" +
                "  \"reviewStrategy\": \"EVERY_DAY\",\n" +
                "  \"category\": " +
                "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"wheel\"\n" +
                "  }" +
                ",\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"bar\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mvc.perform(
                put("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withoutId)
        ).andExpect(status().isBadRequest());

        // 测试缺失title

        //language=JSON
        String withoutTitle = "{\n" +
                "  \"id\": 1,\n" +
                "  \"active\": true,\n" +
                "  \"reviewStrategy\": \"EVERY_DAY\",\n" +
                "  \"category\": " +
                "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"describe\"\n" +
                "  }" +
                ",\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"scorn\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mvc.perform(
                put("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withoutTitle)
        ).andExpect(status().isBadRequest());

        // 测试缺失活跃状态

        //language=JSON
        String withoutActive = "{\n" +
                "  \"id\": 1,\n" +
                "  \"title\": \"fame\",\n" +
                "  \"reviewStrategy\": \"EVERY_DAY\",\n" +
                "  \"category\": " +
                "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"offense\"\n" +
                "  }" +
                ",\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"soon\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mvc.perform(
                put("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withoutActive)
        ).andExpect(status().isBadRequest());

        // 测试缺失关联分类信息

        //language=JSON
        String withoutCategories = "{\n" +
                "  \"id\": 1,\n" +
                "  \"title\": \"what\",\n" +
                "  \"active\": true,\n" +
                "  \"reviewStrategy\": \"EVERY_DAY\",\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"rest\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mvc.perform(
                put("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withoutCategories)
        ).andExpect(status().isOk());

    }
}
