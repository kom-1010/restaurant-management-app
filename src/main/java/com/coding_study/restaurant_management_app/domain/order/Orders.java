package com.coding_study.restaurant_management_app.domain.order;

import com.coding_study.restaurant_management_app.domain.client.Client;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<FoodOrders> foodOrdersList = new ArrayList<>();
    @CreatedDate
    private LocalDateTime orderedAt;
    @Enumerated
    private OrderStatus status;

    @Builder
    public Orders(Client client, List<FoodOrders> foodOrdersList){
        this.client = client;
        for(FoodOrders foodOrders: foodOrdersList) this.foodOrdersList.add(foodOrders);
        status = OrderStatus.PROCESS;
    }

    public void successOrder() {
        status = OrderStatus.SUCCESS;
    }

    public void setFoodOrdersList(List<FoodOrders> foodOrdersList){
        this.foodOrdersList = foodOrdersList;
    }
}
