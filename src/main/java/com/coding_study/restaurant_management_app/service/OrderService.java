package com.coding_study.restaurant_management_app.service;

import com.coding_study.restaurant_management_app.domain.client.Client;
import com.coding_study.restaurant_management_app.domain.client.ClientRepository;
import com.coding_study.restaurant_management_app.domain.food.Food;
import com.coding_study.restaurant_management_app.domain.food.FoodRepository;
import com.coding_study.restaurant_management_app.domain.order.FoodOrder;
import com.coding_study.restaurant_management_app.domain.order.OrderRepository;
import com.coding_study.restaurant_management_app.domain.order.Order;
import com.coding_study.restaurant_management_app.web.dto.FoodCount;
import com.coding_study.restaurant_management_app.web.dto.OrderRequestDto;
import com.coding_study.restaurant_management_app.web.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final ClientRepository clientRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void create(OrderRequestDto requestDto) throws Throwable {
        // 사용자 검증
        Client client = validateClient(requestDto.getClientName(), requestDto.getPassword());

        // 주문 음식 정보 얻기
        List<FoodOrder> foodOrders = makeFoodOrders(requestDto.getFoodCounts());

        // 주문 생성
        Order order = orderRepository.save(Order.builder().client(client).foodOrders(foodOrders).build());

        // 생성된 주문 데이터를 고객의 주문 리스트에 반영
        client.addOrder(order);
        clientRepository.save(client);

        // 주문과 주문 정보 엔티티를 연결
        connectOrderAndFoodOrders(foodOrders, order);
        orderRepository.save(order);
    }

    @Transactional
    public void success(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new IllegalArgumentException("unexpected order id"));

        order.successOrder();
        orderRepository.save(order);
    }

    @Transactional
    public void cancel(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new IllegalArgumentException("unexpected order id"));

        Client client = order.getClient();
        client.removeOrder(order);
        clientRepository.save(client);

        orderRepository.delete(order);
    }

    @Transactional
    public List<OrderResponseDto> read() {
        List<Order> orders = orderRepository.findAll();

        List<OrderResponseDto> responseDtos = new ArrayList<>();
        for(Order order: orders){
            List<FoodCount> foodCounts = new ArrayList<>();
            List<FoodOrder> foodOrders = order.getFoodOrders();

            int totalPrice = 0;
            for(FoodOrder foodOrder: foodOrders){
                FoodCount foodCount = FoodCount.builder()
                        .foodId(foodOrder.getFood().getId())
                        .foodName(foodOrder.getFood().getName())
                        .count(foodOrder.getCount())
                        .build();
                foodCounts.add(foodCount);
                totalPrice += foodOrder.getOrderPrice();
            }

            OrderResponseDto responseDto = OrderResponseDto.builder()
                    .id(order.getId())
                    .clientName(order.getClient().getName())
                    .foodCountList(foodCounts)
                    .orderedAt(order.getOrderedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .status(order.getStatus())
                    .totalPrice(totalPrice)
                    .build();

            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    private void connectOrderAndFoodOrders(List<FoodOrder> foodOrders, Order order) {
        for(FoodOrder foodOrder: foodOrders){
            foodOrder.connectOrder(order);
        }
        order.setFoodOrders(foodOrders);
    }

    private List<FoodOrder> makeFoodOrders(List<FoodCount> foodCounts) throws Throwable {
        List<FoodOrder> foodOrders = new ArrayList<>();
        for(FoodCount foodCount: foodCounts){
            foodOrders.add(FoodOrder.builder()
                    .food((Food) foodRepository.findById(foodCount.getFoodId()).orElseThrow(() ->
                            new IllegalArgumentException("unexpected food")))
                    .count(foodCount.getCount()).build());
        }
        return foodOrders;
    }

    @Transactional
    private Client validateClient(String name, String password){
        Client client = clientRepository.findByNameLike(name).orElseThrow(() ->
                new IllegalArgumentException("unexpected client name"));
        if(!client.getPassword().equals(password))
            throw new IllegalArgumentException("unexpected client password");

        return client;
    }
}
