package com.joanlica.ecommerce.categories.mapper;

import com.joanlica.ecommerce.categories.dto.CategoryDTO;
import com.joanlica.ecommerce.categories.dto.CreateCategoryDTO;
import com.joanlica.ecommerce.categories.model.Category;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryDTO toDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }

    public static Category toEntity(CreateCategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.name().trim());
        return category;
    }
}
