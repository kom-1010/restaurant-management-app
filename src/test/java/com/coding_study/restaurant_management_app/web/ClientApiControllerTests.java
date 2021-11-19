package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.domain.client.Client;
import com.coding_study.restaurant_management_app.domain.client.ClientRepository;
import com.coding_study.restaurant_management_app.web.dto.ClientRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientApiControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClientRepository clientRepository;

    private String name = "client";
    private String password = "12345";

    @AfterEach
    public void tearDown(){
        clientRepository.deleteAll();
    }

    @Test
    public void signup() throws Exception {
        // given
        ClientRequestDto requestDto = ClientRequestDto.builder().name(name).password(password).build();
        String url = "/api/v1/client";

        // when
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
        List<Client> all = clientRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(name);
        assertThat(all.get(0).getPassword()).isEqualTo(password);
    }
}
