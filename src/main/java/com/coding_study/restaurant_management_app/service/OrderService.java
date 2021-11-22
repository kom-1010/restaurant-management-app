package com.coding_study.restaurant_management_app.service;

import com.coding_study.restaurant_management_app.domain.client.Client;
import com.coding_study.restaurant_management_app.domain.client.ClientRepository;
import com.coding_study.restaurant_management_app.domain.food.Food;
import com.coding_study.restaurant_management_app.domain.food.FoodRepository;
import com.coding_study.restaurant_management_app.domain.order.FoodOrders;
import com.coding_study.restaurant_management_app.domain.order.FoodOrdersRepository;
import com.coding_study.restaurant_management_app.domain.order.OrderRepository;
import com.coding_study.restaurant_management_app.domain.order.Orders;
import com.coding_study.restaurant_management_app.web.dto.FoodCount;
import com.coding_study.restaurant_management_app.web.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final ClientRepository clientRepository;
    private final FoodRepository foodRepository;
    private final FoodOrdersRepository foodOrdersRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(OrderRequestDto requestDto) {
        // 사용자 검증
        Client client = validateClient(requestDto.getClientName(), requestDto.getClientPassword());

        List<FoodCount> foodCountList = requestDto.getFoodCountList();
        List<FoodOrders> foodOrdersList = new ArrayList<>();
        for(FoodCount foodCount: foodCountList){
            // 음식 정보 얻기
            Food food = (Food) foodRepository.findById(foodCount.getFoodId()).get();

            // 주문 음식 생성
            foodOrdersList.add(foodOrdersRepository.save(FoodOrders.builder().food(food).count(foodCount.getCount()).build()));
        }

        // 주문 생성
        Orders orders = orderRepository.save(Orders.builder().client(client).foodOrdersList(foodOrdersList).build());

        // 생성된 주문 데이터를 사용자의 주문 리스트에 반영
        client.addOrder(orders);
        clientRepository.save(client);

        // 생성된 주문 데이터를 주문 음식의 주문에 반영
        for(FoodOrders foodOrders: foodOrdersList){
            foodOrders.connectOrder(orders);
            foodOrdersRepository.save(foodOrders);
        }
    }

    @Transactional
    private Client validateClient(String clientName, String clientPassword){
        Client client = clientRepository.findByNameLike(clientName).get();
        if(!client.getPassword().equals(clientPassword))
            throw new IllegalArgumentException("unexpected client");

        return client;
    }

    @Transactional
    public void successOrder(Long orderId) {
        Orders orders = orderRepository.findById(orderId).get();
        orders.successOrder();
        orderRepository.save(orders);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Orders orders = orderRepository.findById(orderId).get();

        foodOrdersRepository.deleteAllByOrderId(orderId);

        Client client = orders.getClient();
        client.removeOrder(orders);
        clientRepository.save(client);

        orderRepository.delete(orders);
    }
}
