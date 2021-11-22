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
import com.coding_study.restaurant_management_app.web.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final ClientRepository clientRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(OrderRequestDto requestDto) {
        // 사용자 검증
        Client client = validateClient(requestDto.getClientName(), requestDto.getClientPassword());

        // 주문 음식 정보 얻기
        List<FoodOrders> foodOrdersList = makeFoodOrdersList(requestDto.getFoodCountList());

        // 주문 생성
        Orders orders = orderRepository.save(Orders.builder().client(client).foodOrdersList(foodOrdersList).build());

        // 생성된 주문 데이터를 고객의 주문 리스트에 반영
        client.addOrder(orders);
        clientRepository.save(client);

        // 주문과 주문 정보 엔티티를 연결
        connectOrderAndFoodOrders(foodOrdersList, orders);
        orderRepository.save(orders);
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

        Client client = orders.getClient();
        client.removeOrder(orders);
        clientRepository.save(client);

        orderRepository.delete(orders);
    }

    private void connectOrderAndFoodOrders(List<FoodOrders> foodOrdersList, Orders orders) {
        for(FoodOrders foodOrders: foodOrdersList){
            foodOrders.connectOrder(orders);
        }
        orders.setFoodOrdersList(foodOrdersList);
    }

    private List<FoodOrders> makeFoodOrdersList(List<FoodCount> foodCountList) {
        List<FoodOrders> foodOrdersList = new ArrayList<>();
        for(FoodCount foodCount: foodCountList){
            foodOrdersList.add(FoodOrders.builder()
                    .food((Food) foodRepository.findById(foodCount.getFoodId()).get())
                    .count(foodCount.getCount()).build());
        }
        return foodOrdersList;
    }

    @Transactional
    private Client validateClient(String clientName, String clientPassword){
        Client client = clientRepository.findByNameLike(clientName).get();
        if(!client.getPassword().equals(clientPassword))
            throw new IllegalArgumentException("unexpected client");

        return client;
    }

    @Transactional
    public List<OrderResponseDto> read() {
        List<Orders> ordersList = orderRepository.findAll();

        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
        for(Orders orders: ordersList){
            List<FoodCount> foodCountList = new ArrayList<>();
            List<FoodOrders> foodOrdersList = orders.getFoodOrdersList();

            int totalPrice = 0;
            for(FoodOrders foodOrders: foodOrdersList){
                FoodCount foodCount = FoodCount.builder()
                        .foodId(foodOrders.getFood().getId())
                        .foodName(foodOrders.getFood().getName())
                        .count(foodOrders.getCount())
                        .build();
                foodCountList.add(foodCount);
                totalPrice += foodOrders.getOrderPrice();
            }

            OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                    .id(orders.getId())
                    .clientName(orders.getClient().getName())
                    .foodCountList(foodCountList)
                    .orderedAt(orders.getOrderedAt())
                    .status(orders.getStatus())
                    .totalPrice(totalPrice)
                    .build();

            orderResponseDtoList.add(orderResponseDto);
        }

        return orderResponseDtoList;
    }
}
