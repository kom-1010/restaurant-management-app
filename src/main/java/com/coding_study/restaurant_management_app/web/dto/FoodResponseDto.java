package com.coding_study.restaurant_management_app.web.dto;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.food.Drink;
import com.coding_study.restaurant_management_app.domain.food.Food;
import com.coding_study.restaurant_management_app.domain.food.Meal;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FoodResponseDto {
    private Long id;
    private String name;
    private int price;
    private int gram;
    private int liter;
    private List<Category> categories;

    public void setMeal(Meal entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.gram = entity.getGram();
        this.categories = entity.getCategories();
    }

    public void setDrink(Drink entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.liter = entity.getLiter();
        this.categories = entity.getCategories();
    }
}
