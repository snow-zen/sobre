package org.snowzen.service;

import org.snowzen.base.IdUtil;
import org.snowzen.exception.NotFoundDataException;
import org.snowzen.model.assembler.CategoryAssembler;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.model.po.CategoryPO;
import org.snowzen.repository.dao.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 分类服务
 *
 * @author snow-zen
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryAssembler categoryAssembler;

    public CategoryService(CategoryRepository categoryRepository, CategoryAssembler categoryAssembler) {
        this.categoryRepository = categoryRepository;
        this.categoryAssembler = categoryAssembler;
    }

    /**
     * 通过id查询分类
     *
     * @param categoryId 分类id
     * @return 查询结果对应的分类DTO
     */
    public CategoryDTO findCategoryById(int categoryId) {
        CategoryPO categoryPO = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundDataException("分类不存在"));

        return categoryAssembler.toDTO(categoryPO);
    }

    /**
     * 添加分类
     *
     * @param categoryDTO 分类DTO，不能为null
     */
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(CategoryDTO categoryDTO) {
        checkNotNull(categoryDTO);

        CategoryPO categoryPO = categoryAssembler.toPO(categoryDTO);
        // 添加前清除id，防止JPA识别为update操作
        categoryPO.setId(null);
        categoryRepository.save(categoryPO);
    }

    /**
     * 通过分类id判断分类是否存在
     *
     * @param categoryId 分类id
     * @return 存在返回true，否则返回false
     */
    public boolean hasCategory(int categoryId) {
        checkArgument(IdUtil.checkId(categoryId), "无效id");

        return categoryRepository.existsById(categoryId);
    }

    /**
     * 保证分类id对应分类必须存在
     *
     * @param categoryId 分类id
     * @throws NotFoundDataException 不存在对应分类时抛出
     */
    public void ensureCategory(int categoryId) {
        checkArgument(IdUtil.checkId(categoryId), "无效id");

        if (!hasCategory(categoryId)) {
            throw new NotFoundDataException("分类不存在");
        }
    }
}
