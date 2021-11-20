package com.coding_study.restaurant_management_app.domain.food;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository<T extends Food> extends JpaRepository<T, Long> {
}
