package com.coding_study.restaurant_management_app.domain.order;

import com.coding_study.restaurant_management_app.domain.client.Client;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<FoodOrder> foodOrders;
    @CreatedDate
    private LocalDateTime orderedAt;
    @Enumerated
    private OrderStatus status;

    @Builder
    public Order(Client client, List<FoodOrder> foodOrders){
        this.client = client;
        this.foodOrders = foodOrders;
        status = OrderStatus.PROCESS;
    }

    public void successOrder() {
        status = OrderStatus.SUCCESS;
    }

    public void setFoodOrders(List<FoodOrder> foodOrders){
        this.foodOrders = foodOrders;
    }
}
