package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 任务实体类
 *
 * @author snow-zen
 */
@Table(name = "task")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskPO extends BaseWithTimePO {

    @Column
    private String title;

    @Column
    private String content;

    @Column(table = "next_review_time")
    private LocalDateTime nextReviewTime;

    @Column
    private Boolean active;
}
