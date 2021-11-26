package com.coding_study.restaurant_management_app.web.dto;

import com.coding_study.restaurant_management_app.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponseDto {
    private Long id;
    private String clientName;
    private List<FoodCount> foodCountList;
    private LocalDateTime orderedAt;
    private OrderStatus status;
    private int totalPrice;

    @Builder
    public OrderResponseDto(Long id, String clientName, List<FoodCount> foodCountList, LocalDateTime orderedAt, OrderStatus status, int totalPrice){
        this.id = id;
        this.clientName = clientName;
        this.foodCountList = foodCountList;
        this.orderedAt = orderedAt;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
