package org.snowzen.model.dto;

import lombok.Data;
import org.snowzen.support.validation.constraints.ValidId;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 分类DTO
 *
 * @author snow-zen
 */
@Data
public class CategoryDTO implements Serializable {

    @ValidId(message = "无效分类id")
    private Integer id;

    @NotBlank(message = "无效分类名称")
    private String name;

}
