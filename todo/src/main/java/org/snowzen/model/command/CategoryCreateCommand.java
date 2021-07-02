package org.snowzen.model.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author snow-zen
 */
@Data
public class CategoryCreateCommand implements Serializable {

    @NotBlank(message = "分类名称不能为空")
    private String name;
}
