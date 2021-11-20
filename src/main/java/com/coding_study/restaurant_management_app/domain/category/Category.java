package com.coding_study.restaurant_management_app.domain.category;

import com.coding_study.restaurant_management_app.domain.food.Food;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "CATEGORY_FOOD",
            joinColumns = @JoinColumn(name = "CATEGORY_ID"),
            inverseJoinColumns = @JoinColumn(name = "FOOD_ID"))
    private List<Food> foods = new ArrayList<Food>();

    public Category(String name){
        this.name = name;
    }

    public void addFood(Food food){
        this.foods.add(food);
    }
}
