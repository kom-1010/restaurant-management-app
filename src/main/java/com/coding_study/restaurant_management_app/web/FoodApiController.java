package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.service.FoodService;
import com.coding_study.restaurant_management_app.web.dto.FoodRequestDto;
import com.coding_study.restaurant_management_app.web.dto.FoodResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class FoodApiController {
    private final FoodService foodService;

    @GetMapping("/foods")
    public List<FoodResponseDto> read(){
        return foodService.read();
    }

    @PostMapping("/foods")
    public void create(@RequestBody FoodRequestDto requestDto){
        foodService.create(requestDto);
    }

    @PutMapping("/foods/{foodId}")
    public void update(@RequestBody FoodRequestDto requestDto, @PathVariable Long foodId) throws Throwable {
        foodService.update(requestDto, foodId);
    }

    @DeleteMapping("/foods/{foodId}")
    public void delete(@PathVariable Long foodId) throws Throwable {
        foodService.delete(foodId);
    }
}
