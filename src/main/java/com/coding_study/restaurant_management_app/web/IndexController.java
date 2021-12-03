package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.service.FoodService;
import com.coding_study.restaurant_management_app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final FoodService foodService;
    private final OrderService orderService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/category")
    public String category(){
        return "category";
    }

    @GetMapping("/food")
    public String food(){
        return "food";
    }

    @GetMapping("/order")
    public String order(Model model){
        model.addAttribute("foods", foodService.read());
        return "order";
    }

    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("orders", orderService.read());
        return "list";
    }
}
