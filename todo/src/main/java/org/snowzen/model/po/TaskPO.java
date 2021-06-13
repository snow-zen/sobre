package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class TaskPO extends BaseWithTimePO {

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

}
