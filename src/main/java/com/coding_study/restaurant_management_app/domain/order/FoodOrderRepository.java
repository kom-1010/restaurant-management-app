package com.coding_study.restaurant_management_app.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM FoodOrder f WHERE f.order.id = ?1")
    void deleteAllByOrderId(Long orderId);

    @Query("SELECT f FROM FoodOrder f WHERE f.order.id = ?1")
    List<FoodOrder> findAllByOrderId(Long id);
}
