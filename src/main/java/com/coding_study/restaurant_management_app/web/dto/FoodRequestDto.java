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
    private int gram;
    private int liter;
    private Category category;

    @Builder
    public FoodRequestDto(String name, String type, int price, int gram, int liter, Category category){
        this.name = name;
        this.type = type;
        this.price = price;
        this.gram = gram;
        this.liter = liter;
        this.category = category;
    }

    public Meal toMeal(){
        return Meal.builder().name(name).price(price).category(category).gram(gram).build();
    }

    public Drink toDrink(){
        return Drink.builder().name(name).price(price).category(category).liter(liter).build();
    }
}
