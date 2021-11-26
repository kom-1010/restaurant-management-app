package com.coding_study.restaurant_management_app.web;

import com.coding_study.restaurant_management_app.domain.category.Category;
import com.coding_study.restaurant_management_app.domain.category.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryApiControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp(){
    }

    @AfterEach
    public void tearDown(){
        categoryRepository.deleteAll();
    }

    private String name = "category";

    @Test
    public void create() throws Exception {
        // given
        String url = "/api/v1/categories";

        // when
        mvc.perform(MockMvcRequestBuilders.post(url).param("name", name))
                .andExpect(status().isOk());

        // then
        Category testCategory = categoryRepository.findAll().get(0);
        assertThat(testCategory.getName()).isEqualTo(name);
    }
}
