package org.snowzen.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 标签DTO
 *
 * @author snow-zen
 */
@Data
public class TagDTO {

    private Integer id;

    private String name;

    private List<TaskDTO> tasks;
}
