package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 关联标签
     */
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "TODO_TASK_TAG",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<TagPO> tags;

    /**
     * 关联分类
     */
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "TODO_TASK_CATEGORY",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private List<CategoryPO> categories;
}
