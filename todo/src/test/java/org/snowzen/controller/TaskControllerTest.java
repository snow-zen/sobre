package org.snowzen.controller;

import org.junit.jupiter.api.Test;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.review.ReviewStrategy;
import org.snowzen.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author snow-zen
 */
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService taskService;

    @Test
    public void testFindTaskById() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.of(2021, 1, 1, 0, 0, 0);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1);
        taskDTO.setTitle("prepare");
        taskDTO.setContent("explore");
        taskDTO.setFinishTime(time);
        taskDTO.setActive(true);
        taskDTO.setReviewStrategy(ReviewStrategy.EVERY_DAY);
        taskDTO.setCreateTime(time);
        taskDTO.setModifiedTime(time);

        when(taskService.findTask(1)).thenReturn(taskDTO);

        mvc.perform(get("/task/1"))
                .andExpect(matchAll(
                        status().isOk(),
                        jsonPath("$.code").value(200),
                        jsonPath("$.msg").value("成功"),
                        jsonPath("$.result.id").value(taskDTO.getId()),
                        jsonPath("$.result.title").value(taskDTO.getTitle()),
                        jsonPath("$.result.content").value(taskDTO.getContent()),
                        jsonPath("$.result.finishTime").value(formatter.format(taskDTO.getFinishTime())),
                        jsonPath("$.result.active").value(taskDTO.getActive()),
                        jsonPath("$.result.reviewStrategy").value(taskDTO.getReviewStrategy().name()),
                        jsonPath("$.result.createTime").value(formatter.format(taskDTO.getCreateTime())),
                        jsonPath("$.result.modifiedTime").value(formatter.format(taskDTO.getModifiedTime())),
                        jsonPath("$.result.categories").doesNotExist(),
                        jsonPath("$.result.tags").doesNotExist()
                ));
    }

}
