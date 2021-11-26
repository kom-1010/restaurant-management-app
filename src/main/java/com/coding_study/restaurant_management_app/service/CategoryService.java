package com.coding_study.restaurant_management_app.service;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public void create(String name){
        categoryRepository.save(new Category(name));
    }
}
