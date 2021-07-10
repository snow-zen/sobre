package org.snowzen.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.snowzen.review.ReviewStrategy;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;

    private Boolean active;

    private ReviewStrategy reviewStrategy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedTime;

    private CategoryDTO category;

    private List<TagDTO> tags;
}
