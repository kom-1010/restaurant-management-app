package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.category.CategoryRepository;
import com.coding_study.restaurant_management_app.domain.client.Client;
import com.coding_study.restaurant_management_app.domain.client.ClientRepository;
import com.coding_study.restaurant_management_app.domain.food.Drink;
import com.coding_study.restaurant_management_app.domain.food.Food;
import com.coding_study.restaurant_management_app.domain.food.FoodRepository;
import com.coding_study.restaurant_management_app.domain.food.Meal;
import com.coding_study.restaurant_management_app.domain.order.*;
import com.coding_study.restaurant_management_app.web.dto.FoodCount;
import com.coding_study.restaurant_management_app.web.dto.OrderRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    private FoodOrdersRepository foodOrdersRepository;

    private Client client;
    private String clientName = "client";
    private String clientPassword = "12345";
    private List<FoodCount> foodCountList = new ArrayList();

    private Meal meal;
    private String mealName = "pizza";
    private int mealPrice = 18000;
    private int mealGram = 100;

    private Drink drink;
    private String drinkName = "cola";
    private int drinkPrice = 2000;
    private int drinkLister = 2;

    private List<FoodOrders> foodOrdersList = new ArrayList<>();

    @BeforeEach
    public void setup(){
        client = clientRepository.save(Client.builder().name(clientName).password(clientPassword).build());
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

        foodCountList.add(new FoodCount(meal.getId(), 1));
        foodCountList.add(new FoodCount(drink.getId(), 2));
    }

    @AfterEach
    public void tearDown(){
        foodOrdersRepository.deleteAll();
        orderRepository.deleteAll();
        foodRepository.deleteAll();
        categoryRepository.deleteAll();
        clientRepository.deleteAll();

        foodCountList.clear();
        foodOrdersList.clear();
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        // given
        OrderRequestDto requestDto = OrderRequestDto
                .builder()
                .clientName(clientName)
                .clientPassword(clientPassword)
                .foodCountList(foodCountList)
                .build();

        String url = "/api/v1/orders";

        // when
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
        List<Orders> all = orderRepository.findAll();
        assertThat(all.get(0).getClient().getName()).isEqualTo(clientName);
        assertThat(all.get(0).getFoodOrdersList().get(0).getFood().getName()).isEqualTo(mealName);
        assertThat(all.get(0).getStatus()).isEqualTo(OrderStatus.PROCESS);
        assertThat(all.get(0).getOrderedAt()).isBefore(LocalDateTime.now());

        List<FoodOrders> foodOrdersList = foodOrdersRepository.findAll();
        assertThat(foodOrdersList.size()).isGreaterThan(1);
        assertThat(foodOrdersList.get(0).getOrders()).isEqualTo(all.get(0));

        List<Client> clients = clientRepository.findAll();
        assertThat(clients.get(0).getOrders().size()).isEqualTo(1);
    }

    @Test
    public void successOrder() throws Exception {
        // given
        foodOrdersList.add(FoodOrders.builder().food(meal).count(1).build());
        foodOrdersList.add(FoodOrders.builder().food(drink).count(2).build());

        Orders orders = orderRepository.save(Orders.builder().client(client).foodOrdersList(foodOrdersList).build());

        String url = "/api/v1/orders/" + orders.getId();

        // when
        mvc.perform(MockMvcRequestBuilders.put(url)).andExpect(status().isOk());

        // then
        List<Orders> all = orderRepository.findAll();
        assertThat(all.get(0).getStatus()).isEqualTo(OrderStatus.SUCCESS);
    }

    @Test
    @Transactional
    public void cancelOrder() throws Exception {
        // given
        foodOrdersList.add(FoodOrders.builder().food(meal).count(1).build());
        foodOrdersList.add(FoodOrders.builder().food(drink).count(2).build());
        Orders orders = orderRepository.save(Orders.builder().client(client).foodOrdersList(foodOrdersList).build());

        client.addOrder(orders);
        clientRepository.save(client);

        String url = "/api/v1/orders/" + orders.getId();

        // when
        mvc.perform(MockMvcRequestBuilders.delete(url)).andExpect(status().isOk());

        // then
        List<Orders> ordersList = orderRepository.findAll();
        List<FoodOrders> foodOrdersList = foodOrdersRepository.findAll();
        List<Client> clientList = clientRepository.findAll();
        assertThat(ordersList.size()).isEqualTo(0);
        assertThat(foodOrdersList.size()).isEqualTo(0);
        assertThat(clientList.get(0).getOrders().size()).isEqualTo(0);

    }
}
