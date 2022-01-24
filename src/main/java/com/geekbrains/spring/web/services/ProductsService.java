package com.geekbrains.spring.web.services;

import com.geekbrains.spring.web.dto.ProductDto;
import com.geekbrains.spring.web.entities.Product;
import com.geekbrains.spring.web.exceptions.ResourceNotFoundException;
import com.geekbrains.spring.web.repositories.ProductsRepository;
import com.geekbrains.spring.web.repositories.specifications.ProductsSpecifications;
import com.geekbrains.spring.web.soap.products.ProductSoap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;

    public Page<Product> findAll(Integer minPrice, Integer maxPrice, String categoryName, String partTitle, Integer page) {
        Specification<Product> spec = Specification.where(null);
        if (minPrice != null) {
            spec = spec.and(ProductsSpecifications.priceGreaterOrEqualsThan(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductsSpecifications.priceLessThanOrEqualsThan(maxPrice));
        }
        if (categoryName != null) {
            spec = spec.and(ProductsSpecifications.categoryLike(categoryName));
        }
        if (partTitle != null) {
            spec = spec.and(ProductsSpecifications.titleLike(partTitle));
        }

        return productsRepository.findAll(spec, PageRequest.of(page - 1, 8));
    }

    public Optional<Product> findById(Long id) {
        return productsRepository.findById(id);
    }

    public void deleteById(Long id) {
        productsRepository.deleteById(id);
    }

    public Product save(Product product) {
        return productsRepository.save(product);
    }

    @Transactional
    public Product update(ProductDto productDto) {
        Product product = productsRepository.findById(productDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Невозможно обновить продукта, не надйен в базе, id: " + productDto.getId()));
        product.setPrice(productDto.getPrice());
        product.setTitle(productDto.getTitle());
        return product;
    }

    public static final Function<Product, ProductSoap> functionEntityToSoap = pr -> {
        ProductSoap p = new ProductSoap();
        p.setId(pr.getId());
        p.setTitle(pr.getTitle());
        p.setPrice(pr.getPrice());
        p.setCategoryName(pr.getCategory().getName());
        return p;
    };

    public List<ProductSoap> soapFindAll() {

        return productsRepository.findAll().stream().map(functionEntityToSoap).collect(Collectors.toList());
    }

    public ProductSoap soapFindById(Long id) {
        return productsRepository.findById(id).stream().map(functionEntityToSoap).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Продукт не найдет, id: " + id));
    }
}
