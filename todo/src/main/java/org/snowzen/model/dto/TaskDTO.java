package org.snowzen.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.snowzen.review.ReviewStrategy;
import org.snowzen.support.validation.ValidGroup.ModifyGroup;
import org.snowzen.support.validation.constraints.ValidId;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
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

    @ValidId(groups = ModifyGroup.class)
    private Integer id;

    @NotBlank
    private String title;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(groups = ModifyGroup.class)
    private LocalDateTime finishTime;

    @NotNull(groups = ModifyGroup.class)
    private Boolean active;

    @NotNull
    private ReviewStrategy reviewStrategy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(groups = ModifyGroup.class)
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(groups = ModifyGroup.class)
    private LocalDateTime modifiedTime;

    private List<CategoryDTO> categories;

    private List<TagDTO> tags;
}
