package org.snowzen.model.po.relation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.snowzen.model.po.BasePO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 分类与任务关联关系保存
 *
 * @author snow-zen
 */
@Table(name = "TODO_TASK_CATEGORY")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryTaskRelationPO extends BasePO {

    /**
     * 关联任务id
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 关联分类id
     */
    @Column(name = "category_id")
    private Integer categoryId;
}
