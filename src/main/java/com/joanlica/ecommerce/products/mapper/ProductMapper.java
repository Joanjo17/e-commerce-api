package com.joanlica.ecommerce.products.mapper;

import com.joanlica.ecommerce.brands.model.Brand;
import com.joanlica.ecommerce.categories.model.Category;
import com.joanlica.ecommerce.products.dto.CreateProductDTO;
import com.joanlica.ecommerce.products.dto.ProductDTO;
import com.joanlica.ecommerce.products.dto.UpdateProductDTO;
import com.joanlica.ecommerce.products.model.Product;

import java.util.Set;
import java.util.stream.Collectors;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductDTO toProductDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getActive(),
                product.getImage(),
                product.getRating(),
                product.getBrand().getName(),
                product.getCategories().stream()
                        .map(Category::getName).collect(Collectors.toSet()));
    }

    public static Product toEntity(CreateProductDTO dto, Brand brand, Set<Category> categories) {
        return Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stock(dto.stock())
                .active(dto.active() != null ? dto.active() : false)
                .image(dto.image() != null ? dto.image() : "")
                .rating(0.0)
                .brand(brand)
                .categories(categories)
                .build();
    }

    public static void updateEntity(Product product, UpdateProductDTO dto, Brand brand, Set<Category> categories) {
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        product.setActive(dto.active() != null ? dto.active() : product.getActive());
        product.setImage(dto.image() != null ? dto.image() : product.getImage());
        product.setBrand(brand);
        product.setCategories(categories);
    }
}