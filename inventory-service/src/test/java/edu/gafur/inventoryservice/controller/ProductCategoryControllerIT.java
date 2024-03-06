package edu.gafur.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gafur.inventoryservice.domain.ProductCategory;
import edu.gafur.inventoryservice.repository.ProductCategoryRepository;
import edu.gafur.inventoryservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ProductCategoryControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private final String urlTemplate = "/api/categories";

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @Test
    public void shouldCreateProductCategorySuccess() throws Exception {
        // given
        ProductCategory productCategoryRequest = ProductCategory.builder()
                .name("cat-1")
                .build();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .post(urlTemplate)
                        .content(objectMapper.writeValueAsString(productCategoryRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("cat-1"));
    }

    @Test
    public void shouldFetchAllProductsSuccess() throws Exception {
        // given
        ProductCategory productCategory = createProductCategory();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(productCategory.getName()));
    }

    @Test
    public void shouldFetchProductCategoryByIdSuccess() throws Exception {
        // given
        ProductCategory productCategory = createProductCategory();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(urlTemplate + "/{productCategoryId}", productCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(productCategory.getName()));
    }

    private ProductCategory createProductCategory(){
        return productCategoryRepository.save(ProductCategory.builder()
                .name("cat-1")
                .build());
    }
}