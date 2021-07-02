package org.snowzen.model.command;

import lombok.Data;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.review.ReviewStrategy;
import org.snowzen.support.validation.constraints.ValidId;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author snow-zen
 */
@Data
public class TaskUpdateCommand implements Serializable {

    @ValidId(message = "无效任务id")
    private Integer id;

    @NotBlank(message = "任务标题不能为空")
    private String title;

    private String content;

    @NotNull(message = "任务状态不能为空")
    private Boolean active;

    @NotNull(message = "任务复习策略不能为空")
    private ReviewStrategy reviewStrategy;

    @Valid
    @NotNull(message = "任务关联分类不能为null")
    private List<CategoryDTO> categories;

    @Valid
    @NotNull(message = "任务关联标签不能为null")
    private List<TagDTO> tags;
}
