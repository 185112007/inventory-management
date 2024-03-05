package edu.gafur.inventoryservice.service;

import edu.gafur.inventoryservice.domain.ProductCategory;
import edu.gafur.inventoryservice.exception.BadRequestException;
import edu.gafur.inventoryservice.exception.NotFoundException;
import edu.gafur.inventoryservice.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategory create(ProductCategory category) {
        try {
            return productCategoryRepository.save(category);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new BadRequestException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ProductCategory findById(Long id) {
        try {
            Optional<ProductCategory> savedCategory = productCategoryRepository.findById(id);
            return savedCategory.orElseThrow(
                    () -> new RuntimeException("Category not found"));
        }catch (IllegalArgumentException ex){
            log.error(ex.getMessage(), ex);
            throw new BadRequestException("Given id is null", HttpStatus.BAD_REQUEST);
        }catch (RuntimeException ex){
            log.error(ex.getMessage(), ex);
            throw new NotFoundException(ex.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    public List<ProductCategory> fetchAll() {
        return productCategoryRepository.findAll();
    }
}
