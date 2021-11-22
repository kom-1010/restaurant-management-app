package com.coding_study.restaurant_management_app.domain;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.category.CategoryRepository;
import com.coding_study.restaurant_management_app.domain.client.Client;
import com.coding_study.restaurant_management_app.domain.client.ClientRepository;
import com.coding_study.restaurant_management_app.domain.food.Drink;
import com.coding_study.restaurant_management_app.domain.food.Food;
import com.coding_study.restaurant_management_app.domain.food.FoodRepository;
import com.coding_study.restaurant_management_app.domain.food.Meal;
import com.coding_study.restaurant_management_app.domain.order.FoodOrders;
import com.coding_study.restaurant_management_app.domain.order.OrderStatus;
import com.coding_study.restaurant_management_app.domain.order.Orders;
import com.coding_study.restaurant_management_app.domain.order.OrderRepository;
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

    private Client client;
    private List<FoodOrders> foodOrdersList = new ArrayList<>();

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

        foodOrdersList.add(FoodOrders.builder().food(meal).count(1).build());
        foodOrdersList.add(FoodOrders.builder().food(drink).count(2).build());
    }

    @AfterEach
    public void tearDown(){
        orderRepository.deleteAll();
        clientRepository.deleteAll();
        foodRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    @Transactional
    public void save(){
        // given
        Orders order = new Orders(client, foodOrdersList);

        // when
        orderRepository.save(order);

        // then
        List<Orders> all = orderRepository.findAll();
        assertThat(all.get(0).getClient().getName()).isEqualTo(client.getName());
        assertThat(all.get(0).getStatus()).isEqualTo(OrderStatus.PROCESS);
        assertThat(all.get(0).getOrderedAt()).isBefore(LocalDateTime.now());
        assertThat(all.get(0).getFoodOrdersList().get(0).getOrderPrice()).isEqualTo(18000);
        assertThat(all.get(0).getFoodOrdersList().get(1).getOrderPrice()).isEqualTo(4000);
    }

    @Test
    public void update(){
        // given
        Orders orders = new Orders(client, foodOrdersList);
        orderRepository.save(orders);

        orders.successOrder();

        // when
        orderRepository.save(orders);

        // then
        List<Orders> all = orderRepository.findAll();
        assertThat(all.get(0).getStatus()).isEqualTo(OrderStatus.SUCCESS);
    }

    @Test
    public void delete(){
        // given
        Orders orders = new Orders(client, foodOrdersList);
        orderRepository.save(orders);

        // when
        orderRepository.delete(orders);

        // then
        assertThat(orderRepository.findAll().size()).isEqualTo(0);
    }
}
