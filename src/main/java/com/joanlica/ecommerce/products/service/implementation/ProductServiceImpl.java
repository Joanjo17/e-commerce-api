package com.joanlica.ecommerce.products.service.implementation;

import com.joanlica.ecommerce.brands.model.Brand;
import com.joanlica.ecommerce.brands.service.BrandService;
import com.joanlica.ecommerce.categories.service.CategoryService;
import com.joanlica.ecommerce.common.exception.ProductNotFoundException;
import com.joanlica.ecommerce.common.util.pages.dto.PageResponse;
import com.joanlica.ecommerce.products.dto.CreateProductDTO;
import com.joanlica.ecommerce.products.dto.ProductDTO;
import com.joanlica.ecommerce.products.dto.UpdateProductDTO;
import com.joanlica.ecommerce.products.mapper.ProductMapper;
import com.joanlica.ecommerce.products.model.Product;
import com.joanlica.ecommerce.products.repository.ProductRepository;
import com.joanlica.ecommerce.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private static final String CACHE_PRODUCT_NAME = "products";

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @Override
    @Cacheable(value = CACHE_PRODUCT_NAME, key = "'page:' + #pageRequest.pageNumber + '-' + #pageRequest.pageSize + '-' + #pageRequest.sort.toString()")
    public PageResponse<ProductDTO> getProducts(Pageable pageRequest) {
        Page<Product> products = productRepository.findAll(pageRequest);
        return PageResponse.from(products.map(ProductMapper::toProductDTO));
    }

    @Override
    @Cacheable(value = CACHE_PRODUCT_NAME, key = "#id")
    public ProductDTO getProductById(UUID id) {
        return productRepository.findProductsById(id)
                .map(ProductMapper::toProductDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_PRODUCT_NAME, allEntries = true)
    public ProductDTO addProduct(CreateProductDTO productDTO) {
        Brand brand = brandService.getEntityById(productDTO.brandId());
        var categories = categoryService.getAllByIds(productDTO.categoriesId());

        Product product = ProductMapper.toEntity(productDTO, brand, categories);

        Product savedProduct = productRepository.save(product);
        return ProductMapper.toProductDTO(savedProduct);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_PRODUCT_NAME, allEntries = true)
    public ProductDTO updateProduct(UUID id, UpdateProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Brand brand = brandService.getEntityById(productDTO.brandId());
        var categories = categoryService.getAllByIds(productDTO.categoriesId());

        ProductMapper.updateEntity(product, productDTO, brand, categories);

        productRepository.save(product);
        return ProductMapper.toProductDTO(product);
    }
}
