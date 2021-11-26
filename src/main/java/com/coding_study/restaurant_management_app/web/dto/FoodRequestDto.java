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
    private int price;
    private int size;
    private Category category;

    @Builder
    public FoodRequestDto(String name, String type, int price, int size, Category category){
        this.name = name;
        this.type = type;
        this.price = price;
        this.size = size;
        this.category = category;
    }

    public Meal toMeal(){
        return Meal.builder().name(name).price(price).category(category).gram(size).build();
    }

    public Drink toDrink(){
        return Drink.builder().name(name).price(price).category(category).liter(size).build();
    }
}
