package com.joanlica.ecommerce.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryDTO(
        @Schema(
                description = "Name of the category",
                example = "Food",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Category name must not be blank")
        String name
){
}