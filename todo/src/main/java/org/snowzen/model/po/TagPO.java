package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.snowzen.model.Convertible;
import org.snowzen.model.dto.TagDTO;
import org.springframework.beans.BeanUtils;

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
public class TagPO extends BasePO implements Convertible<TagDTO> {

    /**
     * 标签名
     */
    @Column
    private String name;

    @Override
    public TagDTO convert() {
        TagDTO tagDTO = new TagDTO();
        BeanUtils.copyProperties(this, tagDTO);
        return tagDTO;
    }

    @Override
    public void reverse(TagDTO tagDTO) {
        BeanUtils.copyProperties(tagDTO, this);
    }
}
