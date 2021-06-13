package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

}
