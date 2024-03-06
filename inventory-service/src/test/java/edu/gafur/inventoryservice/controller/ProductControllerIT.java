package edu.gafur.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gafur.inventoryservice.domain.Product;
import edu.gafur.inventoryservice.domain.ProductCategory;
import edu.gafur.inventoryservice.dto.ProductDto;
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

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class ProductControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private final String urlTemplate = "/api/products";

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @Test
    public void shouldCreateProductSuccess() throws Exception {
        // given
        ProductCategory category = createProductCategory();
        ProductDto productRequest = ProductDto.builder()
                .name("pro-1")
                .description("product number 1")
                .price(BigDecimal.valueOf(1.56))
                .categoryId(category.getId())
                .build();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .post(urlTemplate)
                        .content(objectMapper.writeValueAsString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("pro-1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1.56))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("product number 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(category.getId()));
    }

    @Test
    public void shouldFetchAllProductsSuccess() throws Exception {
        // given
        Product product = createProduct();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(product.getName()));
    }

    @Test
    public void shouldFetchProductByIdSuccess() throws Exception {
        // given
        Product product = createProduct();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(urlTemplate + "/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    public void shouldFetchInventoryTotalSuccess() throws Exception {
        // given
        Product product = createProduct();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(urlTemplate + "/price/total")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(product.getPrice()));
    }

    @Test
    public void shouldUpdateProductSuccess() throws Exception {
        // given
        Product product = createProduct();
        ProductCategory category = product.getCategory();

        ProductDto updatedProduct = ProductDto.builder()
                .name("pro-2")
                .description("product number 2")
                .price(BigDecimal.valueOf(2.34))
                .categoryId(category.getId())
                .build();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .put(urlTemplate + "/{productId}", product.getId())  // Adjust based on your existing data
                        .content(objectMapper.writeValueAsString(updatedProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedProduct.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(updatedProduct.getPrice()));
    }

    @Test
    public void shouldUpdateProductNotFoundFail() throws Exception {
        // given
        Product product = createProduct();
        ProductCategory category = product.getCategory();

        ProductDto updatedProduct = ProductDto.builder()
                .name("pro-2")
                .description("product number 2")
                .price(BigDecimal.valueOf(2.34))
                .categoryId(category.getId())
                .build();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .put(urlTemplate + "/{productId}", 99L)
                        .content(objectMapper.writeValueAsString(updatedProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldDeleteProductSuccess() throws Exception {
        // given
        Product product = createProduct();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/products/{id}", product.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private Product createProduct(){
        ProductCategory productCategory = createProductCategory();
        return productRepository.save(Product.builder()
                        .name("pro-1")
                        .price(BigDecimal.valueOf(1.23))
                        .description("product number 1")
                        .category(productCategory)
                .build());
    }

    private ProductCategory createProductCategory(){
        return productCategoryRepository.save(ProductCategory.builder()
                .name("cat-1")
                .build());
    }

}