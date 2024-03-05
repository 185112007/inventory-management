package edu.gafur.inventoryservice.service;

import edu.gafur.inventoryservice.repository.ProductRepository;
import edu.gafur.inventoryservice.service.mapper.ProductMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ProductServiceTest extends BaseTestCase{
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductCategoryService productCategoryService;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;


}