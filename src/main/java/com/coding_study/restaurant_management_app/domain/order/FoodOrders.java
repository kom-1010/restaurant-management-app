package com.coding_study.restaurant_management_app.domain.order;

import com.coding_study.restaurant_management_app.domain.food.Food;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class FoodOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Food food;
    @ManyToOne
    private Orders orders;
    private int orderPrice;
    private int count;

    @Builder
    public FoodOrders(Food food, int count){
        this.food = food;
        this.count = count;
        this.orderPrice = food.getPrice() * count;
    }

    public void connectOrder(Orders orders){
        this.orders = orders;
    }
}
