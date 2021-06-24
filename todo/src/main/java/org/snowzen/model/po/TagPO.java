package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 标签实体类
 *
 * @author snow-zen
 */
@Table(name = "TODO_TAG")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class TagPO extends BasePO {

    /**
     * 标签名
     */
    @Column
    private String name;

}
