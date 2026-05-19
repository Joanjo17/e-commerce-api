package com.joanlica.ecommerce.categories.service;

import com.joanlica.ecommerce.categories.dto.CategoryDTO;
import com.joanlica.ecommerce.categories.dto.CreateCategoryDTO;
import com.joanlica.ecommerce.categories.dto.UpdateCategoryDTO;
import com.joanlica.ecommerce.categories.model.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
    CategoryDTO createCategory(CreateCategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, UpdateCategoryDTO categoryDTO);


    Set<Category> getAllByIds(Set<Long> categoryIds);
}