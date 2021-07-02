package org.snowzen.controller;

import org.junit.jupiter.api.Test;
import org.snowzen.model.assembler.CategoryAssembler;
import org.snowzen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author snow-zen
 */
@WebMvcTest(
        value = CategoryController.class,
        includeFilters = {@ComponentScan.Filter(type = ASSIGNABLE_TYPE, classes = CategoryAssembler.class)})
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testFindCategoryUsingInvalidId() throws Exception {
        int negativeCategoryId = -1;
        long tooLongCategoryId = Long.MAX_VALUE;
        String misTypeCategoryId = "c9g4jRuX";

        mvc.perform(get("/category/" + negativeCategoryId))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code").value(400),
                        jsonPath("$.result").doesNotExist()
                ));

        mvc.perform(get("/category/" + tooLongCategoryId))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code").value(400),
                        jsonPath("$.result").doesNotExist()
                ));

        mvc.perform(get("/category/" + misTypeCategoryId))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code").value(400),
                        jsonPath("$.result").doesNotExist()
                ));
    }

    @Test
    public void testAddCategoryUsingInvalidArgument() throws Exception {
        String withoutNameBody = "{}";

        mvc.perform(
                post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withoutNameBody)
        ).andExpect(matchAll(
                status().isBadRequest()
        ));
    }
}
