package com.coding_study.restaurant_management_app.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class FoodCount {
    private Long foodId;
    private int count;
    private String foodName;

    @Builder
    public FoodCount(Long foodId, int count, String foodName){
        this.foodId = foodId;
        this.count = count;
        this.foodName = foodName;
    }
}
