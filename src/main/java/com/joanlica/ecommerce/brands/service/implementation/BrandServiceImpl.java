package com.joanlica.ecommerce.brands.service.implementation;

import com.joanlica.ecommerce.brands.dto.BrandDTO;
import com.joanlica.ecommerce.brands.dto.CreateBrandDTO;
import com.joanlica.ecommerce.brands.dto.UpdateBrandDTO;
import com.joanlica.ecommerce.brands.mapper.BrandMapper;
import com.joanlica.ecommerce.brands.model.Brand;
import com.joanlica.ecommerce.brands.repository.BrandRepository;
import com.joanlica.ecommerce.brands.service.BrandService;
import com.joanlica.ecommerce.common.exception.BrandNotFoundException;
import com.joanlica.ecommerce.common.util.pages.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    public PageResponse<BrandDTO> getAllBrands(Pageable pageRequest) {
        Page<Brand> brands = brandRepository.findAll(pageRequest);
        return PageResponse.from(brands.map(BrandMapper::toDTO));
    }

    @Override
    public BrandDTO getBrandById(Long id) {
        return brandRepository.findById(id)
                .map(BrandMapper::toDTO)
                .orElseThrow(() -> new BrandNotFoundException("Brand not found with id: " + id));
    }

    @Override
    @Transactional
    public BrandDTO createBrand(CreateBrandDTO brandDTO) {
        Brand brand = brandRepository.save(BrandMapper.toEntity(brandDTO));
        return BrandMapper.toDTO(brand);
    }

    @Override
    @Transactional
    public BrandDTO updateBrand(Long id, UpdateBrandDTO brandDTO) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Brand not found with id: " + id));
        BrandMapper.updateEntity(brand, brandDTO);
        brandRepository.save(brand);
        return BrandMapper.toDTO(brand);
    }

    @Override
    public Brand getEntityById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Brand not found with id: " + id));
    }
}
