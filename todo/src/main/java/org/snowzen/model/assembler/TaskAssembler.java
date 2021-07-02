package org.snowzen.model.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.snowzen.model.command.TaskCreateCommand;
import org.snowzen.model.command.TaskUpdateCommand;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.TaskPO;

import java.util.Collections;

/**
 * @author snow-zen
 */
@Mapper(componentModel = "spring", imports = Collections.class)
public interface TaskAssembler {

    /**
     * {@link TaskPO}转为{@link TaskDTO}
     *
     * @param taskPO 任务PO
     * @return 任务DTO
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "finishTime", target = "finishTime"),
            @Mapping(source = "active", target = "active"),
            @Mapping(source = "reviewStrategy", target = "reviewStrategy"),
            @Mapping(source = "createTime", target = "createTime"),
            @Mapping(source = "modifiedTime", target = "modifiedTime"),
            @Mapping(target = "categories", ignore = true),
            @Mapping(target = "tags", ignore = true)
    })
    TaskDTO toDTO(TaskPO taskPO);

    /**
     * {@link TaskDTO}转为{@link TaskPO}
     *
     * @param taskDTO 任务DTO
     * @return 任务PO
     */
    TaskPO toPO(TaskDTO taskDTO);

    /**
     * {@link TaskCreateCommand}转换为{@link TaskDTO}
     *
     * @param command 创建任务命令对象
     * @return 任务DTO
     */
    @Mappings({
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "active", target = "active", defaultValue = "true"),
            @Mapping(source = "reviewStrategy", target = "reviewStrategy", defaultValue = "EVERY_DAY"),
            @Mapping(source = "categories", target = "categories", defaultExpression = "java(Collections.emptyList())"),
            @Mapping(source = "tags", target = "tags", defaultExpression = "java(Collections.emptyList())"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "finishTime", ignore = true),
            @Mapping(target = "modifiedTime", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    TaskDTO toDTO(TaskCreateCommand command);

    /**
     * {@link TaskUpdateCommand}转换为{@link TaskDTO}
     *
     * @param command 创建任务命令对象
     * @return 任务DTO
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "active", target = "active"),
            @Mapping(source = "reviewStrategy", target = "reviewStrategy"),
            @Mapping(source = "categories", target = "categories"),
            @Mapping(source = "tags", target = "tags"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "finishTime", ignore = true),
            @Mapping(target = "modifiedTime", ignore = true)
    })
    TaskDTO toDTO(TaskUpdateCommand command);
}
