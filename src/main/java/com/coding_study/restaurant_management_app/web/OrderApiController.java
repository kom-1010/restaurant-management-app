package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.service.OrderService;
import com.coding_study.restaurant_management_app.web.dto.OrderRequestDto;
import com.coding_study.restaurant_management_app.web.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class OrderApiController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public void create(@RequestBody OrderRequestDto requestDto) throws Throwable {
        orderService.create(requestDto);
    }

    @GetMapping("/orders")
    public List<OrderResponseDto> read(){
        return orderService.read();
    }

    @PutMapping("/orders/{orderId}")
    public void success(@PathVariable Long orderId) {
        orderService.success(orderId);
    }

    @DeleteMapping("/orders/{orderId}")
    public void cancel(@PathVariable Long orderId){
        orderService.cancel(orderId);
    }
}
