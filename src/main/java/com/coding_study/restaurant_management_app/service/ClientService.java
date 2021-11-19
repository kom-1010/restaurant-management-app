package com.coding_study.restaurant_management_app.service;

import com.coding_study.restaurant_management_app.domain.client.Client;
import com.coding_study.restaurant_management_app.domain.client.ClientRepository;
import com.coding_study.restaurant_management_app.web.dto.ClientRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public void signup(ClientRequestDto requestDto) {
        clientRepository.save(requestDto.toEntity());
    }
}
