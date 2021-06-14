package org.snowzen.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 标签DTO
 *
 * @author snow-zen
 */
@Data
public class TagDTO implements Serializable {

    private Integer id;

    private String name;

    private List<TaskDTO> tasks;
}
