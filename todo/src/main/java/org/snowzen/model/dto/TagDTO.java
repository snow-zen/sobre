package org.snowzen.model.dto;

import lombok.Data;
import org.snowzen.support.validation.constraints.ValidId;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 标签DTO
 *
 * @author snow-zen
 */
@Data
public class TagDTO implements Serializable {

    @ValidId(message = "无效标签id")
    private Integer id;

    @NotBlank(message = "无效标签名称")
    private String name;
}
