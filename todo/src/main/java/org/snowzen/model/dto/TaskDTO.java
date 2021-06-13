package org.snowzen.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务DTO
 *
 * @author snow-zen
 */
@Data
public class TaskDTO {

    private Integer id;

    private String title;

    private String content;

    private LocalDateTime finishTime;

    private Boolean active;

    private LocalDateTime createTime;

    private LocalDateTime modifiedTime;

    private List<CategoryDTO> categories;

    private List<TagDTO> tags;
}
