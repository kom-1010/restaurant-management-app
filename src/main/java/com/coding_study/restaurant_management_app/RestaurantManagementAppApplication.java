package com.coding_study.restaurant_management_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RestaurantManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantManagementAppApplication.class, args);
	}

}
