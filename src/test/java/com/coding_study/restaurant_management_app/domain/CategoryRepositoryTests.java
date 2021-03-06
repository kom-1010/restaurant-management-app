package com.coding_study.restaurant_management_app.domain;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.category.CategoryRepository;
import com.coding_study.restaurant_management_app.domain.food.Drink;
import com.coding_study.restaurant_management_app.domain.food.FoodRepository;
import com.coding_study.restaurant_management_app.domain.food.Meal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FoodRepository foodRepository;

    private String name = "western food";

    @AfterEach
    public void tearDown(){
        categoryRepository.deleteAll();
        foodRepository.deleteAll();
    }

    @Test
    public void save(){
        // given
        Category category = new Category(name);

        // when
        categoryRepository.save(category);

        // then
        Category testCategory = categoryRepository.findAll().get(0);
        assertThat(testCategory.getName()).isEqualTo(name);
    }

    @Test
    @Transactional
    public void saveFoodTypeMeal(){
        // given
        categoryRepository.save(new Category(name));
        Category category = categoryRepository.findAll().get(0);

        Meal food = Meal.builder().name("pizza").price(18000).category(category).gram(100).build();
        foodRepository.save(food);

        Meal savedFood = (Meal) foodRepository.findAll().get(0);

        // when
        category.addFood(savedFood);
        categoryRepository.save(category);

        // then
        assertThat(category.getFoods().get(0)).isEqualTo(savedFood);
    }

    @Test
    @Transactional
    public void saveFoodTypeDrink(){
        // given
        categoryRepository.save(new Category(name));
        Category category = categoryRepository.findAll().get(0);

        Drink food = Drink.builder().name("pizza").price(18000).category(category).liter(2).build();

        foodRepository.save(food);
        Drink savedFood = (Drink) foodRepository.findAll().get(0);

        // when
        category.addFood(savedFood);
        categoryRepository.save(category);

        // then
        assertThat(category.getFoods().get(0)).isEqualTo(savedFood);
    }
}
