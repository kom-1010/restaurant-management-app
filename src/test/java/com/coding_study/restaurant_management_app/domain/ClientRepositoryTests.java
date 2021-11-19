package com.coding_study.restaurant_management_app.domain;

import com.coding_study.restaurant_management_app.domain.client.Client;
import com.coding_study.restaurant_management_app.domain.client.ClientRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ClientRepositoryTests {
    @Autowired
    private ClientRepository clientRepository;

    private String name = "client";
    private String password = "12345";

    @AfterEach
    public void tearDown(){
        clientRepository.deleteAll();
    }

    @Test
    public void save(){
        // given
        Client client = Client.builder().name(name).password(password).build();

        // when
        clientRepository.save(client);

        // then
        List<Client> all = clientRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo(name);
        assertThat(all.get(0).getPassword()).isEqualTo(password);
    }

    @Test
    public void saveWithDuplicatedName(){
        // given
        clientRepository.save(Client.builder().name(name).password(password).build());

        // when, then
        assertThatThrownBy(() -> clientRepository.save(Client.builder().name(name).password(password).build()))
                .isInstanceOf(RuntimeException.class);

    }

    @Test
    public void findById(){
        // given
        Long id = clientRepository.save(Client.builder().name(name).password(password).build()).getId();

        // when
        Client client = clientRepository.findById(id).get();

        // then
        assertThat(client.getName()).isEqualTo(name);
        assertThat(client.getPassword()).isEqualTo(password);
    }

    @Test
    public void findAllByName(){
        // given
        clientRepository.save(Client.builder().name(name).password(password).build());

        // when
        Client client = clientRepository.findByNameLike(name).get();

        // then
        assertThat(client.getName()).isEqualTo(name);
        assertThat(client.getPassword()).isEqualTo(password);
    }
}
