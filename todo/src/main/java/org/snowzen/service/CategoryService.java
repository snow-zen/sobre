package org.snowzen.service;

import org.snowzen.repository.dao.CategoryRepository;
import org.springframework.stereotype.Service;

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
}
