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

    @Column(name = "create_time")
    protected LocalDateTime createTime;

    @Column(name = "update_time")
    protected LocalDateTime updateTime;
}
