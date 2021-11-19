package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.service.ClientService;
import com.coding_study.restaurant_management_app.web.dto.ClientRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ClientApiController {
    private final ClientService clientService;

    @PostMapping("/client")
    public void signup(@RequestBody ClientRequestDto requestDto){
        clientService.signup(requestDto);
    }
}
