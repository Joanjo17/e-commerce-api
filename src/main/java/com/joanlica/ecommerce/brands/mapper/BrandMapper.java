package com.joanlica.ecommerce.brands.mapper;

import com.joanlica.ecommerce.brands.dto.BrandDTO;
import com.joanlica.ecommerce.brands.dto.CreateBrandDTO;
import com.joanlica.ecommerce.brands.dto.UpdateBrandDTO;
import com.joanlica.ecommerce.brands.model.Brand;

public final class BrandMapper {

    private BrandMapper() {
    }

    public static BrandDTO toDTO(Brand brand) {
        return new BrandDTO(
                brand.getId(),
                brand.getName(),
                brand.getDescription(),
                brand.getLogoUrl());
    }

    public static Brand toEntity(CreateBrandDTO dto) {
        Brand brand = new Brand();
        brand.setName(dto.name().trim());
        brand.setDescription(dto.description() != null ? dto.description().trim() : null);
        brand.setLogoUrl(dto.logoUrl() != null ? dto.logoUrl().trim() : null);
        return brand;
    }

    public static void updateEntity(Brand brand, UpdateBrandDTO dto) {
        brand.setName(dto.name().trim());
        brand.setDescription(dto.description() != null ? dto.description().trim() : brand.getDescription());
        brand.setLogoUrl(dto.logoUrl() != null ? dto.logoUrl().trim() : brand.getLogoUrl());
    }
}
