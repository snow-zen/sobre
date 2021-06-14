package org.snowzen.service;

import org.snowzen.base.IdUtil;
import org.snowzen.exception.NotFoundDataException;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.repository.dao.CategoryRepository;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 分类服务
 *
 * @author snow-zen
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 通过id查询分类
     *
     * @param categoryId 分类id
     * @return 查询结果对应的分类DTO
     */
    public CategoryDTO findCategoryById(int categoryId) {
        checkArgument(IdUtil.checkId(categoryId), "无效id");

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundDataException("分类不存在"))
                .convert();
    }
}
