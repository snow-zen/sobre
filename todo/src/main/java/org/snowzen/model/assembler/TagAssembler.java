package org.snowzen.model.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.snowzen.model.dto.TagDTO;
import org.snowzen.model.po.TagPO;

/**
 * @author snow-zen
 */
@Mapper(componentModel = "spring")
public interface TagAssembler {

    /**
     * {@link TagPO}转为{@link TagDTO}
     *
     * @param tagPO 标签PO
     * @return 标签DTO
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
    })
    TagDTO toDTO(TagPO tagPO);

    /**
     * {@link TagDTO}转为{@link TagPO}
     *
     * @param tagDTO 标签DTO
     * @return 标签PO
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name")
    })
    TagPO toPO(TagDTO tagDTO);
}
