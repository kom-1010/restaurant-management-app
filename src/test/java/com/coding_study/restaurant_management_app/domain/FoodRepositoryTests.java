package com.coding_study.restaurant_management_app.domain;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.category.CategoryRepository;
import com.coding_study.restaurant_management_app.domain.food.Drink;
import com.coding_study.restaurant_management_app.domain.food.FoodRepository;
import com.coding_study.restaurant_management_app.domain.food.Meal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FoodRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FoodRepository foodRepository;

    private String name = "pizza";
    private int price = 18000;

    public void tearDown(){
        categoryRepository.deleteAll();
        foodRepository.deleteAll();
    }

    @Test
    @Transactional
    public void saveTypeMeal(){
        /// given
        Category category = categoryRepository.save(new Category("양식"));
        Meal food = Meal.builder().name(name).price(price).category(category).gram(100).build();

        // when
        foodRepository.save(food);
        Meal savedFood = (Meal) foodRepository.findAll().get(0);

        // then
        assertThat(savedFood.getName()).isEqualTo(name);
        assertThat(savedFood.getPrice()).isEqualTo(price);
        assertThat(savedFood.getCategories().get(0)).isEqualTo(category);
        assertThat(savedFood.getGram()).isEqualTo(100);
    }

    @Test
    @Transactional
    public void saveTypeDrink(){
        /// given
        categoryRepository.save(new Category("양식"));
        Category category = categoryRepository.findAll().get(0);
        Drink food = Drink.builder().name(name).price(price).category(category).liter(2).build();

        // when
        foodRepository.save(food);
        Drink savedFood = (Drink) foodRepository.findAll().get(0);

        // then
        assertThat(savedFood.getName()).isEqualTo(name);
        assertThat(savedFood.getPrice()).isEqualTo(price);
        assertThat(savedFood.getCategories().get(0)).isEqualTo(category);
        assertThat(savedFood.getLiter()).isEqualTo(2);
    }
}
