package com.coding_study.restaurant_management_app.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderRequestDto {
    private String clientName;
    private String password;
    private List<FoodCount> foodCounts = new ArrayList<>();

    @Builder
    public OrderRequestDto(String clientName, String password, List<FoodCount> foodCounts){
        this.clientName = clientName;
        this.password = password;
        for(FoodCount foodCount: foodCounts) this.foodCounts.add(foodCount);
    }
}
