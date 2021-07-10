package org.snowzen.model.command;

import lombok.Data;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.review.ReviewStrategy;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author snow-zen
 */
@Data
public class TaskCreateCommand implements Serializable {

    @NotBlank(message = "任务标题不能为空")
    private String title;

    private String content;

    private Boolean active;

    private ReviewStrategy reviewStrategy;

    @Valid
    private CategoryDTO category;

    @Valid
    private List<TagDTO> tags;
}
