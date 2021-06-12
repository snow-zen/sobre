package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 标签实体类
 *
 * @author snow-zen
 */
@Table(name = "tag")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class TagPO extends BasePO {

    /**
     * 标签名
     */
    @Column
    private String name;

    /**
     * 关联任务
     */
    @OneToMany(mappedBy = "task.tag")
    private List<TaskPO> tasks;
}
