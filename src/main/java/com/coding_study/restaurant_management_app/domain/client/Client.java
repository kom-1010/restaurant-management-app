package com.coding_study.restaurant_management_app.domain.client;

import com.coding_study.restaurant_management_app.domain.order.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIENT_ID")
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();

    @Builder
    public Client(String name, String password){
        this.name = name;
        this.password = password;
    }

    public void addOrder(Order orders){
        this.orders.add(orders);
    }

    public void removeOrder(Order orders){
        this.orders.remove(orders);
    }
}
