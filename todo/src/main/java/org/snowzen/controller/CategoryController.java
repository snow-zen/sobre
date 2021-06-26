package org.snowzen.controller;

import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.service.CategoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 分类控制器
 *
 * @author snow-zen
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("{categoryId}")
    public CategoryDTO category(@PathVariable("categoryId") int categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    @PostMapping
    public void category(@RequestBody @Validated CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
    }
}
