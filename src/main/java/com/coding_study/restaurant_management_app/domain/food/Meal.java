package com.coding_study.restaurant_management_app.domain.food;

import com.coding_study.restaurant_management_app.domain.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("M")
public class Meal extends Food{
    private int gram;

    @Builder
    public Meal(String name, int price, int gram, Category category){
        super(name, price, category);
        this.gram = gram;
    }

    @Override
    public void update(String name, int price, int size) {
        super.update(name, price);
        this.gram = size;
    }
}
