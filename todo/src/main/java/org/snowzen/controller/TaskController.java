package org.snowzen.controller;

import org.snowzen.model.assembler.TaskAssembler;
import org.snowzen.model.command.TaskCreateCommand;
import org.snowzen.model.command.TaskUpdateCommand;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.service.TaskService;
import org.snowzen.support.validation.constraints.ValidId;
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
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskAssembler taskAssembler;

    public TaskController(TaskService taskService, TaskAssembler taskAssembler) {
        this.taskService = taskService;
        this.taskAssembler = taskAssembler;
    }

    @GetMapping("{taskId}")
    public TaskDTO task(@PathVariable("taskId") @ValidId(message = "无效任务id") int taskId) {
        return taskService.findTask(taskId);
    }

    @PostMapping
    public void task(@RequestBody @Validated TaskCreateCommand command) {
        taskService.addTask(taskAssembler.toDTO(command));
    }

    @GetMapping("/list/category/{categoryId}")
    public List<TaskDTO> taskListByCategoryId(@PathVariable("categoryId") @ValidId(message = "无效分类id") int categoryId) {
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
    public void taskReview(@RequestParam("taskId") @ValidId(message = "无效任务id") int taskId) {
        taskService.finishReview(taskId);
    }

    @PutMapping
    public void taskModify(@RequestBody @Validated TaskUpdateCommand command) {
        taskService.modify(taskAssembler.toDTO(command));
    }

    @DeleteMapping("{taskId}")
    public void taskDelete(@PathVariable("taskId") @ValidId(message = "无效任务id") int taskId) {
        taskService.delete(taskId);
    }

}
