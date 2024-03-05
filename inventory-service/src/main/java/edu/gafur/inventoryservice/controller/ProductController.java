package edu.gafur.inventoryservice.controller;

import edu.gafur.inventoryservice.dto.ProductDto;
import edu.gafur.inventoryservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productRequest){
        return new ResponseEntity<>(productService.create(productRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> fetchAllProducts(){
        return new ResponseEntity<>(productService.fetchAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> fetchProductById(@PathVariable("id") Long productId){
        return new ResponseEntity<>(productService.fetchById(productId), HttpStatus.OK);
    }

    @GetMapping("/price/total")
    public ResponseEntity<BigDecimal> inventoryTotalValue(){
        return new ResponseEntity<>(productService.calculateTotalValue(), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long productId, @RequestBody ProductDto productRequest){
        return new ResponseEntity<>(productService.updateProduct(productId, productRequest), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long productId){
        productService.deleteById(productId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
