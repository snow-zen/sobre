package org.snowzen.model.dto;

import lombok.Data;
import org.snowzen.model.ReviewStrategy;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务DTO
 *
 * @author snow-zen
 */
@Data
public class TaskDTO implements Serializable {

    private Integer id;

    private String title;

    private String content;

    private LocalDateTime finishTime;

    private Boolean active;

    private ReviewStrategy reviewStrategy;

    private LocalDateTime createTime;

    private LocalDateTime modifiedTime;

    private List<CategoryDTO> categories;

    private List<TagDTO> tags;
}
