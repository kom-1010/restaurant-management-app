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

        if(requestDto.getType().equals("M")){
            Meal meal = requestDto.toMeal();
            foodRepository.save(meal);
            category.addFood(meal);
        }
        else if(requestDto.getType().equals("D")) {
            Drink drink = requestDto.toDrink();
            foodRepository.save(drink);
            category.addFood(drink);
        }
        else{
            throw new IllegalArgumentException("unexpected food type");
        }
        categoryRepository.save(category);


    }

    @Transactional
    public List<FoodResponseDto> read() {
        List<Food> foods = foodRepository.findAll();
        List<FoodResponseDto> responseDtoList = new ArrayList<>();

        for(int i=0;i<foods.size();i++){
            FoodResponseDto foodResponseDto = new FoodResponseDto();
            Food food = foods.get(i);

            if(food.getClass().equals(Drink.class)){
                foodResponseDto.setDrink((Drink) food);
            }
            else if(food.getClass().equals(Meal.class)){
                foodResponseDto.setMeal((Meal) food);
            }
            responseDtoList.add(foodResponseDto);
        }

        return responseDtoList;
    }

    @Transactional
    public void update(FoodRequestDto requestDto, Long foodId) throws Throwable {
        if(requestDto.getType().equals("M")) {
            Meal food = (Meal) foodRepository.findById(foodId).orElseThrow(() ->
                    new IllegalArgumentException("unexpected foodId"));
            food.update(requestDto.getName(), requestDto.getPrice(), requestDto.getGram(), requestDto.getLiter());
            foodRepository.save(food);
        }
        else if(requestDto.getType().equals("D")){
            Drink food = (Drink) foodRepository.findById(foodId).orElseThrow(() ->
                    new IllegalArgumentException("unexpected foodId"));
            food.update(requestDto.getName(), requestDto.getPrice(), requestDto.getGram(), requestDto.getLiter());
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
