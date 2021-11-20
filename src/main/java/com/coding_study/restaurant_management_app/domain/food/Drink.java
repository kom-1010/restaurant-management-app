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
@DiscriminatorValue("D")
public class Drink extends Food{
    private int liter;

    @Builder
    public Drink(String name, int price, int liter, Category category){
        super(name, price, category);
        this.liter = liter;
    }

    @Override
    public void update(String name, int price, int gram, int liter) {
        update(name, price);
        this.liter = liter;
    }
}
