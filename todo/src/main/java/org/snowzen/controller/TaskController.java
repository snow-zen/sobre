package org.snowzen.controller;

import org.snowzen.model.dto.TaskDTO;
import org.snowzen.service.TaskService;
import org.snowzen.support.validation.ValidGroup.ModifyGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务控制器
 *
 * @author snow-zen
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("{taskId}")
    public TaskDTO task(@PathVariable("taskId") int taskId) {
        return taskService.findTask(taskId);
    }

    @PostMapping
    public void task(@RequestBody @Validated TaskDTO taskDTO) {
        taskService.addTask(taskDTO);
    }

    @GetMapping("/list/category/{categoryId}")
    public List<TaskDTO> taskListByCategoryId(@PathVariable("categoryId") int categoryId) {
        return taskService.findAllByCategoryId(categoryId);
    }

    @GetMapping("/list")
    public List<TaskDTO> taskList(@RequestParam("key") String key) {
        return taskService.findAllByKey(key);
    }

    @GetMapping("/list/review")
    public List<TaskDTO> taskReviewList() {
        return taskService.findAllNeedReview();
    }

    @PutMapping("/review")
    public void taskReview(@RequestParam("taskId") int taskId) {
        taskService.finishReview(taskId);
    }

    @PutMapping
    public void taskModify(@RequestBody @Validated(ModifyGroup.class) TaskDTO taskDTO) {
        taskService.modify(taskDTO);
    }

    @DeleteMapping("{taskId}")
    public void taskDelete(@PathVariable("taskId") int taskId) {
        taskService.delete(taskId);
    }

}
