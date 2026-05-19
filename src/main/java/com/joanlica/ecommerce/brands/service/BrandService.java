package com.joanlica.ecommerce.brands.service;

import com.joanlica.ecommerce.brands.dto.BrandDTO;
import com.joanlica.ecommerce.brands.dto.CreateBrandDTO;
import com.joanlica.ecommerce.brands.dto.UpdateBrandDTO;
import com.joanlica.ecommerce.brands.model.Brand;
import com.joanlica.ecommerce.common.util.pages.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface BrandService {

    PageResponse<BrandDTO> getAllBrands(Pageable pageRequest);

    BrandDTO getBrandById(Long id);

    BrandDTO createBrand(CreateBrandDTO brandDTO);

    BrandDTO updateBrand(Long id, UpdateBrandDTO brandDTO);

    /**
     * Returns the Brand entity by ID.
     * Used internally by other services (e.g. ProductService) to resolve brand references.
     */
    Brand getEntityById(Long id);
}
