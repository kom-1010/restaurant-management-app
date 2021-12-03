package com.coding_study.restaurant_management_app.web.dto;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.food.Drink;
import com.coding_study.restaurant_management_app.domain.food.Meal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FoodRequestDto {
    private String name;
    private String type;
    private String categoryName;
    private int price;
    private int size;

    @Builder
    public FoodRequestDto(String name, String type, int price, int size, String categoryName){
        this.name = name;
        this.type = type;
        this.price = price;
        this.size = size;
        this.categoryName = categoryName;
    }

}
