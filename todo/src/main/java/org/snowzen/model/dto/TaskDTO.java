package org.snowzen.model.dto;

import lombok.Data;
import org.snowzen.review.ReviewStrategy;
import org.snowzen.support.validation.ValidGroup;
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

    @ValidId
    private Integer id;

    @NotBlank
    private String title;

    private String content;

    @Future(groups = ValidGroup.ModifyGroup.class)
    private LocalDateTime finishTime;

    @NotNull(groups = ValidGroup.ModifyGroup.class)
    private Boolean active;

    @NotNull
    private ReviewStrategy reviewStrategy;

    @PastOrPresent(groups = ValidGroup.ModifyGroup.class)
    private LocalDateTime createTime;

    @PastOrPresent(groups = ValidGroup.ModifyGroup.class)
    private LocalDateTime modifiedTime;

    private List<CategoryDTO> categories;

    private List<TagDTO> tags;
}
