package com.joanlica.ecommerce.brands.controller;

import com.joanlica.ecommerce.brands.dto.BrandDTO;
import com.joanlica.ecommerce.brands.dto.CreateBrandDTO;
import com.joanlica.ecommerce.brands.dto.UpdateBrandDTO;
import com.joanlica.ecommerce.brands.service.BrandService;
import com.joanlica.ecommerce.common.util.pages.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brands")
@Tag(name = "Brands", description = "Endpoints for managing product brands.")
public class BrandController {

    private final BrandService brandService;

    @Operation(
            summary = "List brands",
            description = "Returns a paginated list of all brands.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Brands returned successfully",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            )
    )
    @GetMapping
    public ResponseEntity<PageResponse<BrandDTO>> getAllBrands(@ParameterObject Pageable pageRequest) {
        return ResponseEntity.ok(brandService.getAllBrands(pageRequest));
    }

    @Operation(
            summary = "Get brand by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Brand found",
                            content = @Content(schema = @Schema(implementation = BrandDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @Operation(
            summary = "Create brand",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateBrandDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Brand created",
                            content = @Content(schema = @Schema(implementation = BrandDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Brand already exists", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<BrandDTO> createBrand(@RequestBody @Valid CreateBrandDTO brandDTO) {
        BrandDTO brand = brandService.createBrand(brandDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(brand.id())
                .toUri();
        return ResponseEntity.created(location).body(brand);
    }

    @Operation(
            summary = "Update brand",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateBrandDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Brand updated",
                            content = @Content(schema = @Schema(implementation = BrandDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Brand not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Brand name already exists", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable Long id, @RequestBody @Valid UpdateBrandDTO brandDTO) {
        return ResponseEntity.ok(brandService.updateBrand(id, brandDTO));
    }
}
