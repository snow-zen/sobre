package org.snowzen.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.snowzen.model.Convertible;
import org.snowzen.model.dto.CategoryDTO;
import org.springframework.beans.BeanUtils;

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
public class CategoryPO extends BasePO implements Convertible<CategoryDTO> {

    /**
     * 分类名
     */
    @Column
    private String name;

    @Override
    public CategoryDTO convert() {
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(this, categoryDTO);
        return categoryDTO;
    }

    @Override
    public void reverse(CategoryDTO categoryDTO) {
        BeanUtils.copyProperties(categoryDTO, this);
    }
}
