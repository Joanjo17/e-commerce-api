package com.joanlica.ecommerce.categories.service.implementation;

import com.joanlica.ecommerce.categories.model.Category;
import com.joanlica.ecommerce.categories.repository.CategoryRepository;
import com.joanlica.ecommerce.common.exception.CategoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void getAllByIdsReturnsCategoriesWhenAllIdsExist() {
        Category category = new Category(1L, "Electronics", Set.of());

        when(categoryRepository.findAllById(Set.of(1L))).thenReturn(List.of(category));

        Set<Category> categories = categoryService.getAllByIds(Set.of(1L));

        assertThat(categories).containsExactly(category);
    }

    @Test
    void getAllByIdsThrowsWhenAnyIdIsMissing() {
        when(categoryRepository.findAllById(Set.of(1L, 2L)))
                .thenReturn(List.of(new Category(1L, "Electronics", Set.of())));

        assertThatThrownBy(() -> categoryService.getAllByIds(Set.of(1L, 2L)))
                .isInstanceOf(CategoryNotFoundException.class);
    }
}
