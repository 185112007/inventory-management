package edu.gafur.inventoryservice.service.mapper;

import edu.gafur.inventoryservice.domain.Product;
import edu.gafur.inventoryservice.domain.ProductCategory;
import edu.gafur.inventoryservice.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    public ProductDto toDto(Product entity){
        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .categoryId(entity.getCategory().getId())
                .build();
    }

    public List<ProductDto> toDto(List<Product> entities){
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Product toEntity(ProductDto dto, ProductCategory category){
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .category(category)
                .build();
    }
}
