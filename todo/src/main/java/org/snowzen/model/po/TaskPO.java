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
@Table(name = "task")
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
    @Column(table = "finish_time")
    private LocalDateTime finishTime;

    /**
     * 活动状态
     */
    @Column
    private Boolean active;

    /**
     * 关联标签
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tag")
    private List<TagPO> tags;

    /**
     * 关联分类
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<CategoryPO> categories;
}
