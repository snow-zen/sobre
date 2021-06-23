package org.snowzen.model.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.po.CategoryPO;

/**
 * 分类组装器
 *
 * @author snow-zen
 */
@Mapper(componentModel = "spring")
public interface CategoryAssembler {

    /**
     * {@link CategoryPO}转为{@link CategoryDTO}
     *
     * @param categoryPO 分类PO
     * @return 分类DTO
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(target = "tasks", ignore = true)
    })
    CategoryDTO PO2DTO(CategoryPO categoryPO);

    /**
     * {@link CategoryDTO}转为{@link CategoryPO}
     *
     * @param categoryDTO 分类DTO
     * @return 分类PO
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name")
    })
    CategoryPO DTO2PO(CategoryDTO categoryDTO);
}
