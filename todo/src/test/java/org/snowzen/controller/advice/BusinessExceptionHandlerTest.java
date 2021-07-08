package org.snowzen.controller.advice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.snowzen.controller.advice.BusinessExceptionHandler.NOT_FOUND_DATA;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author snow-zen
 */
@WebMvcTest(ExceptionTestController.class)
public class BusinessExceptionHandlerTest {


    @Autowired
    private MockMvc mvc;

    @Test
    public void testNotFoundDataException() throws Exception {
        mvc.perform(get("/exception/NotFoundDataException"))
                .andExpect(matchAll(
                        status().isInternalServerError(),
                        jsonPath("$.code").value(500),
                        jsonPath("$.msg").value(NOT_FOUND_DATA)
                ));
    }
}
