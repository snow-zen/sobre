package org.snowzen.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 分类DTO
 *
 * @author snow-zen
 */
@Data
public class CategoryDTO {

    private Integer id;

    private String name;

    private List<TaskDTO> tasks;
}
