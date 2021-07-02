package org.snowzen.model.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.snowzen.model.command.CategoryCreateCommand;
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
            @Mapping(source = "name", target = "name")
    })
    CategoryDTO toDTO(CategoryPO categoryPO);

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
    CategoryPO toPO(CategoryDTO categoryDTO);

    /**
     * {@link CategoryCreateCommand}转为{@link CategoryDTO}
     *
     * @param command 分类创建命令对象
     * @return 分类DTO
     */
    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(target = "id", ignore = true)
    })
    CategoryDTO toDTO(CategoryCreateCommand command);
}
