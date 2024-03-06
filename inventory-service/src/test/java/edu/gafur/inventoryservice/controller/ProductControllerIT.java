package edu.gafur.inventoryservice.controller;

import edu.gafur.inventoryservice.domain.Product;
import edu.gafur.inventoryservice.domain.ProductCategory;
import edu.gafur.inventoryservice.repository.ProductCategoryRepository;
import edu.gafur.inventoryservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @Test
    public void testDeleteProduct() throws Exception {
        // given
        Product product = createProduct();

        // when and then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/products/{id}", product.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private Product createProduct(){
        ProductCategory productCategory = productCategoryRepository.save(ProductCategory.builder()
                .name("cat-1")
                .build());
        return productRepository.save(Product.builder()
                        .name("pro-1")
                        .price(BigDecimal.valueOf(1.23))
                        .description("product number 1")
                        .category(productCategory)
                .build());
    }

}