package com.joanlica.ecommerce.products.service;

import com.joanlica.ecommerce.common.util.pages.dto.PageResponse;
import com.joanlica.ecommerce.products.dto.CreateProductDTO;
import com.joanlica.ecommerce.products.dto.ProductDTO;
import com.joanlica.ecommerce.products.dto.UpdateProductDTO;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    PageResponse<ProductDTO> getProducts(Pageable pageRequest);
    ProductDTO getProductById(UUID id);
    ProductDTO addProduct(CreateProductDTO productDTO);
    ProductDTO updateProduct(UUID id, UpdateProductDTO productDTO);
}
