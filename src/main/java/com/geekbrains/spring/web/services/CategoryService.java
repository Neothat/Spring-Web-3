package com.geekbrains.spring.web.services;

import com.geekbrains.spring.web.entities.Category;
import com.geekbrains.spring.web.exceptions.ResourceNotFoundException;
import com.geekbrains.spring.web.repositories.CategoryRepository;
import com.geekbrains.spring.web.soap.categories.CategorySoap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Категория не найдена в базе, name: " + name));
    }

    public static final Function<Category, CategorySoap> functionEntityToSoap = ct -> {
        CategorySoap c = new CategorySoap();
        c.setId(ct.getId());
        c.setName(ct.getName());
        ct.getProducts().stream().map(ProductsService.functionEntityToSoap).forEach(p -> c.getProducts().add(p));
        return c;
    };

    public CategorySoap soapFindByName(String name) {
        return categoryRepository.findByName(name).map(functionEntityToSoap).get();
    }
}
