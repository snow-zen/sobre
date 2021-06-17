package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.snowzen.model.Convertible;
import org.snowzen.model.ReviewStrategy;
import org.snowzen.model.dto.TaskDTO;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 任务实体类
 *
 * @author snow-zen
 */
@Table(name = "TODO_TASK")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskPO extends BaseWithTimePO implements Convertible<TaskDTO> {

    /**
     * 标题
     */
    @Column
    private String title;

    /**
     * 内容
     */
    @Column
    private String content;

    /**
     * 完成时间
     */
    @Column(name = "finish_time")
    private LocalDateTime finishTime;

    /**
     * 活动状态
     */
    @Column(name = "is_active")
    private Boolean active;

    /**
     * 复习策略
     */
    @Column(name = "review_strategy")
    private ReviewStrategy reviewStrategy;

    @Override
    public void prePersistCallback() {
        super.prePersistCallback();
        active = Boolean.TRUE;
    }

    @Override
    public TaskDTO convert() {
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(this, taskDTO);
        return taskDTO;
    }

    @Override
    public void reverse(TaskDTO taskDTO) {
        BeanUtils.copyProperties(taskDTO, this);
    }
}
