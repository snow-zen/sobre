package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 分类实体类
 *
 * @author snow-zen
 */
@Table(name = "TODO_CATEGORY")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryPO extends BasePO {

    /**
     * 分类名
     */
    @Column
    private String name;

    /**
     * 关联分类
     */
    @ManyToMany(mappedBy = "categories")
    private List<TaskPO> tasks;
}
