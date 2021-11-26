package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CategoryApiController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    public void create(@RequestParam String name){
        categoryService.create(name);
    }
}
