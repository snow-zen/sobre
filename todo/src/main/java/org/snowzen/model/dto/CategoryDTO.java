package org.snowzen.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分类DTO
 *
 * @author snow-zen
 */
@Data
public class CategoryDTO implements Serializable {

    private Integer id;

    private String name;

    private List<TaskDTO> tasks;
}
