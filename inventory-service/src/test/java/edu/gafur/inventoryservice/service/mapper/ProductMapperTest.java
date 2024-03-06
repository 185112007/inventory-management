package edu.gafur.inventoryservice.service.mapper;

import edu.gafur.inventoryservice.domain.Product;
import edu.gafur.inventoryservice.domain.ProductCategory;
import edu.gafur.inventoryservice.dto.ProductDto;
import edu.gafur.inventoryservice.service.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest extends BaseTestCase {
    private final ProductMapper productMapper = new ProductMapper();

    @Test
    public void testEntityToDtoSuccess(){
        // given
        long productId = 1L;
        String proName = "pro-1";
        String desc = "product number 1";
        BigDecimal price = BigDecimal.valueOf(1.23);
        long catId = 2L;
        String catName = "cat-2";
        Product entity = Product.builder()
                .id(productId)
                .name(proName)
                .description(desc)
                .price(price)
                .category(ProductCategory.builder()
                        .id(catId)
                        .name(catName)
                        .build())
                .build();
        ProductDto productDto = ProductDto.builder()
                .id(productId)
                .name(proName)
                .description(desc)
                .price(price)
                .categoryId(catId)
                .build();

        // when
        ProductDto dto = productMapper.toDto(entity);

        // then
        assertThat(dto.getId()).isEqualTo(productDto.getId());
        assertThat(dto.getName()).isEqualTo(productDto.getName());
        assertThat(dto.getDescription()).isEqualTo(productDto.getDescription());
        assertThat(dto.getPrice()).isEqualTo(productDto.getPrice());
        assertThat(dto.getCategoryId()).isEqualTo(productDto.getCategoryId());
    }

    @Test
    public void testEntitiesToDtoSuccess(){
        // given
        List<Product> products = List.of(
                Product.builder()
                        .id(1L)
                        .name("pro-1")
                        .description("product number 1")
                        .price(BigDecimal.valueOf(1.34))
                        .category(ProductCategory.builder()
                                .id(1L)
                                .name("cat-1")
                                .build())
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("pro-2")
                        .description("product number 2")
                        .price(BigDecimal.valueOf(1.43))
                        .category(ProductCategory.builder()
                                .id(1L)
                                .name("cat-1")
                                .build())
                        .build()
        );
        List<ProductDto> productDtos = List.of(
                ProductDto.builder()
                        .id(1L)
                        .name("pro-1")
                        .description("product number 1")
                        .price(BigDecimal.valueOf(1.34))
                        .categoryId(1L)
                        .build(),
                ProductDto.builder()
                        .id(2L)
                        .name("pro-2")
                        .description("product number 2")
                        .price(BigDecimal.valueOf(1.43))
                        .categoryId(1L)
                        .build()
        );

        // when
        List<ProductDto> dtos = productMapper.toDto(products);

        // then
        assertThat(dtos.size()).isEqualTo(productDtos.size());
    }

    @Test
    public void testDtoToEntitySuccess(){
        // given
        long productId = 1L;
        String proName = "pro-1";
        String desc = "product number 1";
        BigDecimal price = BigDecimal.valueOf(1.23);
        long catId = 2L;
        String catName = "cat-2";
        ProductCategory category = ProductCategory.builder()
                .id(catId)
                .name(catName)
                .build();
        Product entity = Product.builder()
                .id(productId)
                .name(proName)
                .description(desc)
                .price(price)
                .category(category)
                .build();
        ProductDto productDto = ProductDto.builder()
                .id(productId)
                .name(proName)
                .description(desc)
                .price(price)
                .categoryId(catId)
                .build();

        // when
        Product productEntity = productMapper.toEntity(productDto, category);

        // then
        assertThat(productEntity.getId()).isEqualTo(entity.getId());
        assertThat(productEntity.getName()).isEqualTo(entity.getName());
        assertThat(productEntity.getDescription()).isEqualTo(entity.getDescription());
        assertThat(productEntity.getPrice()).isEqualTo(entity.getPrice());
        assertThat(productEntity.getCategory().getId()).isEqualTo(entity.getCategory().getId());
        assertThat(productEntity.getCategory().getName()).isEqualTo(entity.getCategory().getName());
    }
}