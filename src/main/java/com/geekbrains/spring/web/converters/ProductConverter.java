package com.geekbrains.spring.web.converters;

import com.geekbrains.spring.web.dto.ProductDto;
import com.geekbrains.spring.web.entities.Product;
import com.geekbrains.spring.web.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConverter {

    private final CategoryService categoryService;

    public Product dtoToEntity(ProductDto productDto) {
        return new Product(productDto.getId(), productDto.getTitle(), productDto.getPrice(), categoryService.findByName(productDto.getCategory()));
    }

    public ProductDto entityToDto(Product product) {
        return new ProductDto(product.getId(), product.getTitle(), product.getPrice(), product.getCategory().getName());
    }
}
