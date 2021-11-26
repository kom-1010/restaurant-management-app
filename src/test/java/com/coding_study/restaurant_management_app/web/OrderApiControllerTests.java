package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.category.CategoryRepository;
import com.coding_study.restaurant_management_app.domain.client.Client;
import com.coding_study.restaurant_management_app.domain.client.ClientRepository;
import com.coding_study.restaurant_management_app.domain.food.Drink;
import com.coding_study.restaurant_management_app.domain.food.FoodRepository;
import com.coding_study.restaurant_management_app.domain.food.Meal;
import com.coding_study.restaurant_management_app.domain.order.*;
import com.coding_study.restaurant_management_app.web.dto.FoodCount;
import com.coding_study.restaurant_management_app.web.dto.OrderRequestDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderApiControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FoodOrderRepository foodOrderRepository;

    private Client client;
    private String clientName = "client";
    private String password = "12345";
    private List<FoodCount> foodCounts;

    private Meal meal;
    private String mealName = "pizza";
    private int mealPrice = 18000;
    private int mealGram = 100;

    private Drink drink;
    private String drinkName = "cola";
    private int drinkPrice = 2000;
    private int drinkLister = 2;

    private List<FoodOrder> foodOrders;

    @BeforeEach
    public void setup(){
        foodCounts = new ArrayList<>();
        foodOrders = new ArrayList<>();

        client = clientRepository.save(Client.builder().name(clientName).password(password).build());
        Category category = categoryRepository.save(new Category("양식"));
        meal = ((Meal) foodRepository.save(Meal.builder()
                .name(mealName)
                .price(mealPrice)
                .gram(mealGram)
                .category(category)
                .build()));

        drink = ((Drink) foodRepository.save(Drink.builder()
                .name(drinkName)
                .price(drinkPrice)
                .liter(drinkLister)
                .category(category)
                .build()));

        foodCounts.add(FoodCount.builder().foodId(meal.getId()).count(1).build());
        foodCounts.add(FoodCount.builder().foodId(drink.getId()).count(2).build());
    }

    @AfterEach
    public void tearDown(){
        foodOrderRepository.deleteAll();
        orderRepository.deleteAll();
        foodRepository.deleteAll();
        categoryRepository.deleteAll();
        clientRepository.deleteAll();

        foodCounts.clear();
        foodOrders.clear();
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        // given
        OrderRequestDto requestDto = OrderRequestDto
                .builder()
                .clientName(clientName)
                .password(password)
                .foodCounts(foodCounts)
                .build();

        String url = "/api/v1/orders";

        // when
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
        Order testOrder = orderRepository.findAll().get(0);
        assertThat(testOrder.getClient().getName()).isEqualTo(clientName);
        assertThat(testOrder.getFoodOrders().get(0).getFood().getName()).isEqualTo(mealName);
        assertThat(testOrder.getStatus()).isEqualTo(OrderStatus.PROCESS);
        assertThat(testOrder.getOrderedAt()).isBefore(LocalDateTime.now());

        List<FoodOrder> testFoodOrders = foodOrderRepository.findAll();
        assertThat(testFoodOrders.size()).isGreaterThan(1);
        assertThat(testFoodOrders.get(0).getOrder()).isEqualTo(testOrder);

        Client testClient = clientRepository.findAll().get(0);
        assertThat(testClient.getOrders().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void read() throws Exception {
        // given
        foodOrders.add(FoodOrder.builder().food(meal).count(1).build());
        foodOrders.add(FoodOrder.builder().food(drink).count(2).build());
        Order order = Order.builder().client(client).foodOrders(foodOrders).build();
        orderRepository.save(order);

        String url = "/api/v1/orders";

        // when
        ResultActions actions = mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk());

        // then
        actions
                .andExpect(jsonPath("$[0].clientName").value(clientName))
                .andExpect(jsonPath("$[0].status").value(OrderStatus.PROCESS.name()))
                .andExpect(jsonPath("$[0].foodCountList[0].foodName").value(mealName))
                .andExpect(jsonPath("$[0].totalPrice").value(22000));
    }

    @Test
    public void successOrder() throws Exception {
        // given
        foodOrders.add(FoodOrder.builder().food(meal).count(1).build());
        foodOrders.add(FoodOrder.builder().food(drink).count(2).build());

        Order order = orderRepository.save(Order.builder().client(client).foodOrders(foodOrders).build());

        String url = "/api/v1/orders/" + order.getId();

        // when
        mvc.perform(MockMvcRequestBuilders.put(url)).andExpect(status().isOk());

        // then
        Order testOrder = orderRepository.findAll().get(0);
        assertThat(testOrder.getStatus()).isEqualTo(OrderStatus.SUCCESS);
    }

    @Test
    @Transactional
    public void cancelOrder() throws Exception {
        // given
        foodOrders.add(FoodOrder.builder().food(meal).count(1).build());
        foodOrders.add(FoodOrder.builder().food(drink).count(2).build());
        Order order = orderRepository.save(Order.builder().client(client).foodOrders(foodOrders).build());

        client.addOrder(order);
        clientRepository.save(client);

        String url = "/api/v1/orders/" + order.getId();

        // when
        mvc.perform(MockMvcRequestBuilders.delete(url)).andExpect(status().isOk());

        // then
        List<Order> testOrders = orderRepository.findAll();
        List<FoodOrder> testFoodOrders = foodOrderRepository.findAll();
        Client testClient = clientRepository.findAll().get(0);
        assertThat(testOrders.size()).isEqualTo(0);
        assertThat(testFoodOrders.size()).isEqualTo(0);
        assertThat(testClient.getOrders().size()).isEqualTo(0);
    }
}
