package com.joanlica.ecommerce.products.controller;

import com.joanlica.ecommerce.common.util.pages.dto.PageResponse;
import com.joanlica.ecommerce.products.dto.CreateProductDTO;
import com.joanlica.ecommerce.products.dto.ProductDTO;
import com.joanlica.ecommerce.products.dto.UpdateProductDTO;
import com.joanlica.ecommerce.products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Slf4j
@Tag(name = "Products", description = "Endpoints for managing products in the e-commerce application.")
public class ProductController {

        private final ProductService productService;

        @Operation(summary = "List products", responses = @ApiResponse(responseCode = "200", description = "Products returned successfully", content = @Content(schema = @Schema(implementation = PageResponse.class))))
        @GetMapping
        public ResponseEntity<PageResponse<ProductDTO>> getProducts(@ParameterObject Pageable pageRequest) {
                return ResponseEntity.ok(productService.getProducts(pageRequest));
        }

        @Operation(summary = "Get product by ID", responses = {
                        @ApiResponse(responseCode = "200", description = "Product found", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
        })
        @GetMapping("/{id}")
        public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
                return ResponseEntity.ok(productService.getProductById(id));
        }

        @Operation(summary = "Create product", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = CreateProductDTO.class))), responses = {
                        @ApiResponse(responseCode = "201", description = "Product created", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
        })
        @PostMapping
        public ResponseEntity<ProductDTO> addProduct(@RequestBody @Valid CreateProductDTO productDTO) {
                log.info("Adding product {}", productDTO.name());
                ProductDTO product = productService.addProduct(productDTO);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(product.id())
                                .toUri();
                return ResponseEntity.created(location).body(product);
        }

        @Operation(summary = "Update product", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = UpdateProductDTO.class))), responses = {
                        @ApiResponse(responseCode = "200", description = "Product updated", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Product or category not found", content = @Content)
        })
        @PutMapping("/{id}")
        public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id,
                        @RequestBody @Valid UpdateProductDTO productDTO) {
                log.info("Updating product {}", productDTO.name());
                ProductDTO product = productService.updateProduct(id, productDTO);

                return ResponseEntity.ok(product);
        }
}
