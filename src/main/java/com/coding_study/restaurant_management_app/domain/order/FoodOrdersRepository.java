package com.coding_study.restaurant_management_app.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FoodOrdersRepository extends JpaRepository<FoodOrders, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM FoodOrders f WHERE f.orders.id = ?1")
    void deleteAllByOrderId(Long orderId);
}
