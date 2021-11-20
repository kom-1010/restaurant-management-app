package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.category.CategoryRepository;
import com.coding_study.restaurant_management_app.domain.food.*;
import com.coding_study.restaurant_management_app.web.dto.FoodRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FoodApiControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FoodRepository foodRepository;

    private String mealName = "pizza";
    private String drinkName = "colla";
    private int mealPrice = 18000;
    private int drinkPrice = 2000;
    private int gram = 100;
    private int liter = 2;
    private Category category;

    @BeforeEach
    public void setup(){
        category = categoryRepository.save(new Category("양식"));
    }

    @AfterEach
    public void tearDown(){
        categoryRepository.deleteAll();
        foodRepository.deleteAll();
    }

    @Test
    @Transactional
    public void create() throws Exception {
        // given
        FoodRequestDto requestDto = FoodRequestDto.builder().name(mealName).type("M").price(mealPrice).category(category).gram(gram).build();
        String url = "/api/v1/foods";

        // when
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
        List<Meal> all = foodRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(mealName);
        assertThat(all.get(0).getPrice()).isEqualTo(mealPrice);
        assertThat(all.get(0).getCategories().get(0).getName()).isEqualTo(category.getName());
        assertThat(all.get(0).getGram()).isEqualTo(gram);
    }

    @Test
    public void read_when_only_meal_type_is_included() throws Exception {
        // given
        foodRepository.save(Meal.builder().name(mealName).price(mealPrice).gram(gram).category(category).build());
        String url = "/api/v1/foods";

        // when
        ResultActions actions = mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk());

        // then
        actions
                .andExpect(jsonPath("$[0].name").value(mealName))
                .andExpect(jsonPath("$[0].price").value(mealPrice))
                .andExpect(jsonPath("$[0].gram").value(gram));
    }

    @Test
    public void read_when_only_drink_type_is_included() throws Exception {
        // given
        foodRepository.save(Drink.builder().name(drinkName).price(drinkPrice).liter(liter).category(category).build());
        String url = "/api/v1/foods";

        // when
        ResultActions actions = mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk());

        // then
        actions
                .andExpect(jsonPath("$[0].name").value(drinkName))
                .andExpect(jsonPath("$[0].price").value(drinkPrice))
                .andExpect(jsonPath("$[0].liter").value(liter));
    }

    @Test
    public void read_when_meal_type_and_drink_type_are_included() throws Exception {
        // given
        foodRepository.save(Drink.builder().name(drinkName).price(drinkPrice).liter(liter).category(category).build());
        foodRepository.save(Meal.builder().name(mealName).price(mealPrice).gram(gram).category(category).build());
        String url = "/api/v1/foods";

        // when
        ResultActions actions = mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk());

        // then
        actions
                .andExpect(jsonPath("$[0].name").value(drinkName))
                .andExpect(jsonPath("$[0].price").value(drinkPrice))
                .andExpect(jsonPath("$[0].liter").value(liter))
                .andExpect(jsonPath("$[1].name").value(mealName))
                .andExpect(jsonPath("$[1].price").value(mealPrice))
                .andExpect(jsonPath("$[1].gram").value(gram));
    }

    @Test
    public void update() throws Exception {
        // given
        Long id = ((Meal) foodRepository.save(Meal.builder().name(mealName).price(mealPrice).gram(gram).category(category).build())).getId();
        String modifiedName = "chicken";
        int modifiedPrice = 15000;
        int modifiedGram = 200;

        FoodRequestDto requestDto = FoodRequestDto.builder()
                .name(modifiedName)
                .price(modifiedPrice)
                .category(category)
                .gram(modifiedGram)
                .type("M")
                .build();

        String url = "/api/v1/foods/" +id;

        // when
        mvc.perform(MockMvcRequestBuilders.put(url)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
        List<Meal> all = foodRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(modifiedName);
        assertThat(all.get(0).getPrice()).isEqualTo(modifiedPrice);
        assertThat(all.get(0).getGram()).isEqualTo(modifiedGram);
    }

    @Test
    public void delete() throws Exception {
        // given
        Long id = ((Meal) foodRepository.save(Meal.builder().name(mealName).price(mealPrice).gram(gram).category(category).build())).getId();
        String url = "/api/v1/foods/" + id;

        // when, then
        mvc.perform(MockMvcRequestBuilders.delete(url)).andExpect(status().isOk());
    }
}
