package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.service.OrderService;
import com.coding_study.restaurant_management_app.web.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class OrderApiController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderRequestDto requestDto){
        orderService.createOrder(requestDto);
    }

    @PutMapping("/orders/{orderId}")
    public void successOrder(@PathVariable Long orderId) {
        orderService.successOrder(orderId);
    }

    @DeleteMapping("/orders/{orderId}")
    public void cancelOrder(@PathVariable Long orderId){
        orderService.cancelOrder(orderId);
    }
}
