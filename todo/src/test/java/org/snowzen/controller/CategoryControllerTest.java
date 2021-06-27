package org.snowzen.controller;

import org.junit.jupiter.api.Test;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author snow-zen
 */
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testFindCategoryById() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("heaven");

        when(categoryService.findCategoryById(1))
                .thenReturn(categoryDTO);

        mvc.perform(get("/category/1"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$.code").value(200),
                        jsonPath("$.result.id").value(categoryDTO.getId()),
                        jsonPath("$.result.name").value(categoryDTO.getName())
                ));
    }

    @Test
    public void testAddCategory() throws Exception {
        //language=JSON
        String input = "{\"name\": \"report\"}";

        doNothing().when(categoryService).addCategory(any(CategoryDTO.class));

        // @formatter:off
        mvc.perform(
                post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input)
            )
            .andExpect(matchAll(
                    status().isOk(),
                    jsonPath("$.code").value(200)
            ));
        // @formatter:on
    }
}
