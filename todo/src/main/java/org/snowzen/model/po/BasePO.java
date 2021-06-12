package org.snowzen.model.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 基础实体类
 *
 * @author snow-zen
 */
@MappedSuperclass
@Data
public abstract class BasePO {

    /**
     * 主键id
     */
    @Id
    protected Integer id;

}
