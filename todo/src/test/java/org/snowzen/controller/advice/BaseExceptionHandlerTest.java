package org.snowzen.controller.advice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.snowzen.constant.ExceptionMessage.SYSTEM_ERROR;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author snow-zen
 */
@WebMvcTest(ExceptionTestController.class)
public class BaseExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testNullPointerException() throws Exception {
        mvc.perform(get("/exception/NullPointerException"))
                .andExpect(matchAll(
                        status().isInternalServerError(),
                        jsonPath("$.code").value(500),
                        jsonPath("$.msg").value(SYSTEM_ERROR)
                ));
    }

    @Test
    public void testIllegalStateException() throws Exception {
        mvc.perform(get("/exception/IllegalStateException"))
                .andExpect(matchAll(
                        status().isBadRequest(),
                        jsonPath("$.code").value(400)
                ));
    }


}
