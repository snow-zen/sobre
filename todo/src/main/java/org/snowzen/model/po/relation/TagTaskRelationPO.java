package org.snowzen.model.po.relation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.snowzen.model.po.BasePO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 标签与任务关联关系实体
 *
 * @author snow-zen
 */
@Table(name = "TODO_TASK_TAG")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class TagTaskRelationPO extends BasePO {

    /**
     * 关联任务id
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 关联标签id
     */
    @Column(name = "tag_id")
    private Integer tagId;
}
