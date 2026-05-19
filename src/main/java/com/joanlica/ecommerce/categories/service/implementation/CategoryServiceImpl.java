package com.joanlica.ecommerce.categories.service.implementation;

import com.joanlica.ecommerce.categories.dto.CategoryDTO;
import com.joanlica.ecommerce.categories.dto.CreateCategoryDTO;
import com.joanlica.ecommerce.categories.dto.UpdateCategoryDTO;
import com.joanlica.ecommerce.categories.mapper.CategoryMapper;
import com.joanlica.ecommerce.categories.model.Category;
import com.joanlica.ecommerce.categories.repository.CategoryRepository;
import com.joanlica.ecommerce.categories.service.CategoryService;
import com.joanlica.ecommerce.common.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toDTO)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Override
    public CategoryDTO createCategory(CreateCategoryDTO categoryDTO) {
        var category = categoryRepository.save(CategoryMapper.toEntity(categoryDTO));
        return CategoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(Long id, UpdateCategoryDTO categoryDTO) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        category.setName(categoryDTO.name().trim());
        categoryRepository.save(category);
        return CategoryMapper.toDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Category> getAllByIds(Set<Long> categoryIds){
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));

        if (categories.size() != categoryIds.size()) {
            throw new CategoryNotFoundException("One or more categories were not found");
        }

        return categories;
    }
}