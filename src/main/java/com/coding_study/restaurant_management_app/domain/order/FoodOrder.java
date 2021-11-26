package com.coding_study.restaurant_management_app.domain.order;

import com.coding_study.restaurant_management_app.domain.food.Food;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Food food;
    @ManyToOne
    private Order order;
    private int orderPrice;
    private int count;

    @Builder
    public FoodOrder(Food food, int count){
        this.food = food;
        this.count = count;
        this.orderPrice = food.getPrice() * count;
    }

    public void connectOrder(Order order){
        this.order = order;
    }
}
