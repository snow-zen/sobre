package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author snow-zen
 */
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseWithTimePO extends BasePO {

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "gmt_modified")
    protected LocalDateTime modifiedTime;
}
