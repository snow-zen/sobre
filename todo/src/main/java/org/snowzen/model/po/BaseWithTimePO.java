package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * 携带创建时间与更新时间的基础实体类
 *
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

    @PrePersist
    public void prePersistCallback() {
        LocalDateTime now = LocalDateTime.now();
        createTime = now;
        modifiedTime = now;
    }

    @PreUpdate
    public void preUpdateCallback() {
        modifiedTime = LocalDateTime.now();
    }
}
