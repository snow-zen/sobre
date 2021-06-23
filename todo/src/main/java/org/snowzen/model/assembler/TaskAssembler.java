package org.snowzen.model.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.snowzen.model.dto.TaskDTO;
import org.snowzen.model.po.TaskPO;

/**
 * @author snow-zen
 */
@Mapper(componentModel = "spring")
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
    TaskDTO PO2DTO(TaskPO taskPO);

    /**
     * {@link TaskDTO}转为{@link TaskPO}
     *
     * @param taskDTO 任务DTO
     * @return 任务PO
     */
    TaskPO DTO2PO(TaskDTO taskDTO);
}
