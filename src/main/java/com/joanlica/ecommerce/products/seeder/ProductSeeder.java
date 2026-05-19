package com.joanlica.ecommerce.products.seeder;

import com.joanlica.ecommerce.brands.model.Brand;
import com.joanlica.ecommerce.brands.repository.BrandRepository;
import com.joanlica.ecommerce.categories.model.Category;
import com.joanlica.ecommerce.categories.repository.CategoryRepository;
import com.joanlica.ecommerce.products.model.Product;
import com.joanlica.ecommerce.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile("dev")
@Slf4j
@RequiredArgsConstructor
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (productRepository.count() > 0) {
            log.info("Database already contains products.");
            return;
        }

        log.info("Seeding product data...");

        Category electronics = new Category(null, "Electronics", new HashSet<>());
        Category gaming = new Category(null, "Gaming", new HashSet<>());
        Category office = new Category(null, "Office", new HashSet<>());

        categoryRepository.saveAll(List.of(electronics, gaming, office));

        Brand techBrand = new Brand();
        techBrand.setName("TechPro");
        techBrand.setDescription("Premium technology products for professionals.");

        Brand furnitureBrand = new Brand();
        furnitureBrand.setName("ErgoDesk");
        furnitureBrand.setDescription("Ergonomic office furniture and accessories.");

        brandRepository.saveAll(List.of(techBrand, furnitureBrand));

        Product laptop = Product.builder()
                .name("Laptop Pro X")
                .description("High-end laptop ideal for software development and design.")
                .price(new BigDecimal("1499.99"))
                .stock(25)
                .active(true)
                .image("https://example.com/images/laptop.jpg")
                .rating(4.8)
                .brand(techBrand)
                .categories(Set.of(electronics, office))
                .build();

        Product mouse = Product.builder()
                .name("Wireless RGB Mouse")
                .description("Ergonomic mouse with customizable lighting.")
                .price(new BigDecimal("45.50"))
                .stock(100)
                .active(true)
                .image("https://example.com/images/mouse.jpg")
                .rating(4.5)
                .brand(techBrand)
                .categories(Set.of(electronics, gaming))
                .build();

        Product desk = Product.builder()
                .name("Adjustable Standing Desk")
                .description("Desk with motorized height adjustment.")
                .price(new BigDecimal("350.00"))
                .stock(10)
                .active(false)
                .image("https://example.com/images/desk.jpg")
                .rating(4.2)
                .brand(furnitureBrand)
                .categories(Set.of(office))
                .build();

        productRepository.saveAll(List.of(laptop, mouse, desk));

        log.info("Seed data loaded successfully.");
    }
}
