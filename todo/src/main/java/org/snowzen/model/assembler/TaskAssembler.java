package org.snowzen.model.assembler;

import org.snowzen.model.command.TaskCreateCommand;
import org.snowzen.model.command.TaskUpdateCommand;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.TaskPO;
import org.snowzen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author snow-zen
 */
@Component
public class TaskAssembler {

    @Autowired
    private CategoryService categoryService;

    /**
     * {@link TaskPO}转为{@link TaskDTO}
     *
     * @param po 任务PO
     * @return 任务DTO
     */
    public TaskDTO toDTO(TaskPO po) {
        TaskDTO dto = new TaskDTO();
        dto.setId(po.getId());
        dto.setTitle(po.getTitle());
        dto.setContent(po.getContent());
        dto.setFinishTime(po.getFinishTime());
        dto.setActive(po.getActive());
        dto.setReviewStrategy(po.getReviewStrategy());
        dto.setCreateTime(po.getCreateTime());
        dto.setModifiedTime(po.getModifiedTime());
        if (po.getCategoryId() != null) {
            dto.setCategory(categoryService.findCategoryById(po.getCategoryId()));
        }
        return dto;
    }
    /**
     * {@link TaskDTO}转为{@link TaskPO}
     *
     * @param dto 任务DTO
     * @return 任务PO
     */
    public TaskPO toPO(TaskDTO dto) {
        TaskPO po = new TaskPO();
        po.setId(dto.getId());
        po.setTitle(dto.getTitle());
        po.setContent(dto.getContent());
        po.setFinishTime(dto.getFinishTime());
        po.setActive(dto.getActive());
        po.setReviewStrategy(dto.getReviewStrategy());
        po.setCreateTime(dto.getCreateTime());
        po.setModifiedTime(dto.getModifiedTime());
        if (dto.getCategory() != null) {
            po.setCategoryId(dto.getCategory().getId());
        }
        return po;
    }

    /**
     * {@link TaskCreateCommand}转换为{@link TaskDTO}
     *
     * @param command 创建任务命令对象
     * @return 任务DTO
     */
    public TaskDTO toDTO(TaskCreateCommand command) {
        TaskDTO dto = new TaskDTO();
        dto.setTitle(command.getTitle());
        dto.setContent(command.getContent());
        dto.setActive(command.getActive());
        dto.setReviewStrategy(command.getReviewStrategy());
        dto.setCategory(command.getCategory());
        dto.setTags(command.getTags());
        return dto;
    }

    /**
     * {@link TaskUpdateCommand}转换为{@link TaskDTO}
     *
     * @param command 创建任务命令对象
     * @return 任务DTO
     */
    public TaskDTO toDTO(TaskUpdateCommand command) {
        TaskDTO dto = new TaskDTO();
        dto.setId(command.getId());
        dto.setTitle(command.getTitle());
        dto.setContent(command.getContent());
        dto.setActive(command.getActive());
        dto.setReviewStrategy(command.getReviewStrategy());
        dto.setCategory(command.getCategory());
        dto.setTags(command.getTags());
        return dto;
    }
}
