package com.coding_study.restaurant_management_app.web.dto;

import com.coding_study.restaurant_management_app.domain.client.Client;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClientRequestDto {
    private String name;
    private String password;

    @Builder
    public ClientRequestDto(String name, String password){
        this.name = name;
        this.password = password;
    }

    public Client toEntity(){
        return Client.builder().name(name).password(password).build();
    }
}
