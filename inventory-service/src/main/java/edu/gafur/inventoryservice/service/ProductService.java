package edu.gafur.inventoryservice.service;

import edu.gafur.inventoryservice.domain.Product;
import edu.gafur.inventoryservice.domain.ProductCategory;
import edu.gafur.inventoryservice.dto.ProductDto;
import edu.gafur.inventoryservice.exception.BadRequestException;
import edu.gafur.inventoryservice.exception.NotFoundException;
import edu.gafur.inventoryservice.repository.ProductRepository;
import edu.gafur.inventoryservice.service.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final ProductMapper productMapper;

    public ProductDto create(ProductDto product) {
        try {
            ProductCategory category = productCategoryService.findById(product.getCategoryId());
            Product productEntity = productMapper.toEntity(product, category);
            productEntity = productRepository.save(productEntity);
            return productMapper.toDto(productEntity);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new BadRequestException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public List<ProductDto> fetchAll() {
        return productMapper.toDto(productRepository.findAll());
    }

    public ProductDto fetchById(Long productId) {
        Product product = productById(productId);
        return productMapper.toDto(product);
    }

    public ProductDto updateProduct(Long productId, ProductDto productRequest) {
        Product existingProduct = productById(productId);
        ProductCategory category = productCategoryService.findById(productRequest.getCategoryId());
        productRequest.setId(existingProduct.getId());
        Product newProduct = productMapper.toEntity(productRequest, category);
        return productMapper.toDto(productRepository.save(newProduct));
    }

    public void deleteById(Long productId) {
        try {
            productRepository.deleteById(productId);
        }catch (IllegalArgumentException ex){
            log.error(ex.getMessage(), ex);
            throw new BadRequestException("Given id is null", HttpStatus.BAD_REQUEST);
        }
    }

    private Product productById(Long id){
        try {
            Optional<Product> savedProduct = productRepository.findById(id);
            return savedProduct.orElseThrow(
                    () -> new NotFoundException("Product with id:" + id + " not found", HttpStatus.NOT_FOUND));
        }catch (IllegalArgumentException ex){
            log.error(ex.getMessage(), ex);
            throw new BadRequestException("Given id is null", HttpStatus.BAD_REQUEST);
        }

    }

    public BigDecimal calculateTotalValue() {
        return productRepository.totalProductPrice();
    }
}
