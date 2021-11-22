package com.coding_study.restaurant_management_app.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderRequestDto {
    private String clientName;
    private String clientPassword;
    private List<FoodCount> foodCountList = new ArrayList<>();

    @Builder
    public OrderRequestDto(String clientName, String clientPassword, List<FoodCount> foodCountList){
        this.clientName = clientName;
        this.clientPassword = clientPassword;
        for(FoodCount foodCount: foodCountList) this.foodCountList.add(foodCount);
    }
}
