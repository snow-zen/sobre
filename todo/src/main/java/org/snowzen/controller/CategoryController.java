package org.snowzen.controller;

import org.snowzen.model.assembler.CategoryAssembler;
import org.snowzen.model.command.CategoryCreateCommand;
import org.snowzen.model.dto.CategoryDTO;
import org.snowzen.service.CategoryService;
import org.snowzen.support.validation.constraints.ValidId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 分类控制器
 *
 * @author snow-zen
 */
@RestController
@RequestMapping("/category")
@Validated
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryAssembler categoryAssembler;

    public CategoryController(CategoryService categoryService, CategoryAssembler categoryAssembler) {
        this.categoryService = categoryService;
        this.categoryAssembler = categoryAssembler;
    }

    @GetMapping("{categoryId}")
    public CategoryDTO category(@PathVariable("categoryId") @ValidId(message = "无效id") int categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    @PostMapping
    public void category(@RequestBody @Validated CategoryCreateCommand command) {
        categoryService.addCategory(categoryAssembler.toDTO(command));
    }
}
