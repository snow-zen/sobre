package org.snowzen.model.dto;

import lombok.Data;
import org.snowzen.support.validation.ValidGroup.ModifyGroup;
import org.snowzen.support.validation.constraints.ValidId;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 分类DTO
 *
 * @author snow-zen
 */
@Data
public class CategoryDTO implements Serializable {

    @ValidId(groups = ModifyGroup.class)
    private Integer id;

    @NotBlank
    private String name;

    private List<TaskDTO> tasks;
}
