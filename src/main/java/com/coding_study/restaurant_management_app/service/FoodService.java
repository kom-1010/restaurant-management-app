package com.coding_study.restaurant_management_app.service;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.category.CategoryRepository;
import com.coding_study.restaurant_management_app.domain.food.*;
import com.coding_study.restaurant_management_app.web.dto.FoodRequestDto;
import com.coding_study.restaurant_management_app.web.dto.FoodResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FoodService {
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void create(FoodRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.getCategory().getId()).orElseThrow(() ->
                new IllegalArgumentException("unexpected category"));

        Food food;
        if(requestDto.getType().equals("M"))
            food = (Food) foodRepository.save(requestDto.toMeal());
        else if(requestDto.getType().equals("D"))
            food = (Food) foodRepository.save(requestDto.toDrink());
         else
            throw new IllegalArgumentException("unexpected food type");

        category.addFood(food);
        categoryRepository.save(category);
    }

    @Transactional
    public List<FoodResponseDto> read() {
        List<Food> foods = foodRepository.findAll();
        List<FoodResponseDto> responseDtos = new ArrayList<>();

        for(int i=0;i<foods.size();i++){
            FoodResponseDto responseDto = new FoodResponseDto();
            Food food = foods.get(i);

            if(food.getClass().equals(Drink.class))
                responseDto.setDrink((Drink) food);
            else    // food.getClass().equals(Meal.class)
                responseDto.setMeal((Meal) food);

            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @Transactional
    public void update(FoodRequestDto requestDto, Long foodId) throws Throwable {
        if(requestDto.getType().equals("M")) {
            Meal food = (Meal) foodRepository.findById(foodId).orElseThrow(() ->
                    new IllegalArgumentException("unexpected foodId"));
            food.update(requestDto.getName(), requestDto.getPrice(), requestDto.getSize());
            foodRepository.save(food);
        }
        else if(requestDto.getType().equals("D")){
            Drink food = (Drink) foodRepository.findById(foodId).orElseThrow(() ->
                    new IllegalArgumentException("unexpected foodId"));
            food.update(requestDto.getName(), requestDto.getPrice(), requestDto.getSize());
            foodRepository.save(food);
        }
        else{
            throw new IllegalArgumentException("unexpected foodId");
        }
    }

    @Transactional
    public void delete(Long foodId) throws Throwable {
        Food food = (Food) foodRepository.findById(foodId).orElseThrow(() ->
                new IllegalArgumentException("unexpected foodId"));

        foodRepository.delete(food);
    }
}
