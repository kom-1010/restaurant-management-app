package com.coding_study.restaurant_management_app.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

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
    public String order(){
        return "order";
    }

    @GetMapping("/list")
    public String list(){
        return "list";
    }
}
