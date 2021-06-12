package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 分类实体类
 *
 * @author snow-zen
 */
@Table(name = "category")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryPO extends BasePO {

    /**
     * 分类名
     */
    @Column
    private String name;

    @OneToMany(mappedBy = "task.category")
    private List<TaskPO> tasks;
}
