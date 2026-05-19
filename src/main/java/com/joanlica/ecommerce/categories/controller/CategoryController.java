package com.joanlica.ecommerce.categories.controller;

import com.joanlica.ecommerce.categories.dto.CategoryDTO;
import com.joanlica.ecommerce.categories.dto.CreateCategoryDTO;
import com.joanlica.ecommerce.categories.dto.UpdateCategoryDTO;
import com.joanlica.ecommerce.categories.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Categories", description = "Endpoints for managing product categories.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "List categories",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Categories returned successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))
            )
    )
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(
            summary = "Get category by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category found",
                            content = @Content(schema = @Schema(implementation = CategoryDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(
            summary = "Create category",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateCategoryDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Category created",
                            content = @Content(schema = @Schema(implementation = CategoryDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Category already exists", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CreateCategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.createCategory(categoryDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(category.id())
                .toUri();
        return ResponseEntity.created(location)
                .body(category);
    }

    @Operation(
            summary = "Update category",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateCategoryDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category updated",
                            content = @Content(schema = @Schema(implementation = CategoryDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Category already exists", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody @Valid UpdateCategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(category);

    }
}
