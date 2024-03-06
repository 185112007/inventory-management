package edu.gafur.inventoryservice.controller;

import edu.gafur.inventoryservice.domain.ProductCategory;
import edu.gafur.inventoryservice.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @PostMapping
    public ResponseEntity<ProductCategory> createProductCategory(@RequestBody ProductCategory category){
        return new ResponseEntity<>(productCategoryService.create(category), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductCategory>> fetchAllCategories(){
        return new ResponseEntity<>(productCategoryService.fetchAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductCategory> findCategoryById(@PathVariable("id") Long id){
        return new ResponseEntity<>(productCategoryService.findById(id), HttpStatus.OK);
    }
}
