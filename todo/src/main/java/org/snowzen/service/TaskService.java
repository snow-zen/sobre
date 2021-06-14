package org.snowzen.service;

import org.snowzen.repository.dao.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务服务
 *
 * @author snow-zen
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
