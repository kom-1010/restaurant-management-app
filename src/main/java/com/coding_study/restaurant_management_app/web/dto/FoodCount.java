package com.coding_study.restaurant_management_app.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class FoodCount {
    private Long foodId;
    private int count;

    public FoodCount(Long foodId, int count){
        this.foodId = foodId;
        this.count = count;
    }
}
