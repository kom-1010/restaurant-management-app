package com.coding_study.restaurant_management_app.domain;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.category.CategoryRepository;
import com.coding_study.restaurant_management_app.domain.client.Client;
import com.coding_study.restaurant_management_app.domain.client.ClientRepository;
import com.coding_study.restaurant_management_app.domain.food.Drink;
import com.coding_study.restaurant_management_app.domain.food.FoodRepository;
import com.coding_study.restaurant_management_app.domain.food.Meal;
import com.coding_study.restaurant_management_app.domain.order.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrdersRepositoryTests {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FoodOrderRepository foodOrderRepository;

    private Client client;
    private List<FoodOrder> foodOrders = new ArrayList<>();

    @BeforeEach
    public void setup(){
        client = clientRepository.save(Client.builder().name("client").password("12345").build());
        Meal meal = (Meal) foodRepository.save(Meal.builder()
                .name("pizza")
                .price(18000)
                .gram(100)
                .category(categoryRepository.save(new Category("양식")))
                .build());
        Drink drink = (Drink) foodRepository.save(Drink.builder()
                .name("cola")
                .price(2000)
                .liter(2)
                .category(categoryRepository.save(new Category("양식")))
                .build());

        foodOrders.add(FoodOrder.builder().food(meal).count(1).build());
        foodOrders.add(FoodOrder.builder().food(drink).count(2).build());
    }

    @AfterEach
    public void tearDown(){
        foodOrderRepository.deleteAll();
        orderRepository.deleteAll();
        clientRepository.deleteAll();
        foodRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    @Transactional
    public void save(){
        // given
        Order order = new Order(client, foodOrders);

        // when
        orderRepository.save(order);

        // then
        Order testOrder = orderRepository.findAll().get(0);
        assertThat(testOrder.getClient().getName()).isEqualTo(client.getName());
        assertThat(testOrder.getStatus()).isEqualTo(OrderStatus.PROCESS);
        assertThat(testOrder.getOrderedAt()).isBefore(LocalDateTime.now());
        assertThat(testOrder.getFoodOrders().get(0).getOrderPrice()).isEqualTo(18000);
        assertThat(testOrder.getFoodOrders().get(1).getOrderPrice()).isEqualTo(4000);
    }

    @Test
    public void update(){
        // given
        Order order = new Order(client, foodOrders);
        orderRepository.save(order);
        order.successOrder();

        // when
        orderRepository.save(order);

        // then
        Order testOrder = orderRepository.findAll().get(0);
        assertThat(testOrder.getStatus()).isEqualTo(OrderStatus.SUCCESS);
    }

    @Test
    public void delete(){
        // given
        Order order = new Order(client, foodOrders);
        orderRepository.save(order);

        // when
        orderRepository.delete(order);

        // then
        assertThat(orderRepository.findAll().size()).isEqualTo(0);
    }
}
