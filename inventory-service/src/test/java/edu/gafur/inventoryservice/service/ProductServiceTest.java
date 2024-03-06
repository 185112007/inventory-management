package edu.gafur.inventoryservice.service;

import edu.gafur.inventoryservice.domain.Product;
import edu.gafur.inventoryservice.domain.ProductCategory;
import edu.gafur.inventoryservice.dto.ProductDto;
import edu.gafur.inventoryservice.exception.BadRequestException;
import edu.gafur.inventoryservice.exception.base.AppBaseException;
import edu.gafur.inventoryservice.repository.ProductRepository;
import edu.gafur.inventoryservice.service.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest extends BaseTestCase {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductCategoryService productCategoryService;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    public void shouldCreateProductSuccess() {
        // given
        long id = 1L;
        String categoryName = "cat-1";
        String proName = "pro-1";
        String desc = "product number 1";
        BigDecimal price = BigDecimal.valueOf(1.23);

        ProductCategory category = ProductCategory.builder()
                .id(id)
                .name(categoryName)
                .build();
        ProductDto productDto = ProductDto.builder()
                .name(proName)
                .description(desc)
                .price(price)
                .categoryId(id)
                .build();
        Product product = Product.builder()
                .name(proName)
                .description(desc)
                .price(price)
                .category(category)
                .build();
        Product savedPro = Product.builder()
                .id(id)
                .name(proName)
                .description(desc)
                .price(price)
                .category(category)
                .build();
        ProductDto savedProductDto = ProductDto.builder()
                .id(id)
                .name(proName)
                .description(desc)
                .price(price)
                .categoryId(id)
                .build();
        when(productCategoryService.findById(id)).thenReturn(category);
        when(productMapper.toEntity(productDto, category)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedPro);
        when(productMapper.toDto(savedPro)).thenReturn(savedProductDto);

        // when
        ProductDto pro = productService.create(productDto);

        // then
        assertThat(pro.getId()).isEqualTo(id);
        assertThat(pro.getName()).isEqualTo(proName);
        assertThat(pro.getDescription()).isEqualTo(desc);
        assertThat(pro.getPrice()).isEqualTo(price);
        assertThat(pro.getCategoryId()).isEqualTo(id);
    }

    @Test
    public void shouldCreateProductWithNullCategoryIdFail() {
        // given
        Long categoryId = null;
        String proName = "pro-1";
        String desc = "product number 1";
        BigDecimal price = BigDecimal.valueOf(1.23);

        ProductDto productDto = ProductDto.builder()
                .name(proName)
                .description(desc)
                .price(price)
                .categoryId(categoryId)
                .build();

        String errorMsg = "Given id is null";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        when(productCategoryService.findById(categoryId))
                .thenThrow(new BadRequestException(errorMsg, httpStatus));

        // when
        AppBaseException exception = assertThrows(
                BadRequestException.class,
                () -> productService.create(productDto));

        // then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
        assertThat(exception.getStatus()).isEqualTo(httpStatus);
    }

    @Test
    public void shouldCreateNullProductFail() {
        // given
        long id = 1L;
        String categoryName = "cat-1";
        String proName = "pro-1";
        String desc = "product number 1";
        BigDecimal price = BigDecimal.valueOf(1.23);

        ProductCategory category = ProductCategory.builder()
                .id(id)
                .name(categoryName)
                .build();
        ProductDto productDto = ProductDto.builder()
                .name(proName)
                .description(desc)
                .price(price)
                .categoryId(id)
                .build();
        Product product = null;
        when(productCategoryService.findById(id)).thenReturn(category);
        when(productMapper.toEntity(productDto, category)).thenReturn(product);
        when(productRepository.save(product))
                .thenThrow(new IllegalArgumentException());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        // when
        AppBaseException exception = assertThrows(
                BadRequestException.class,
                () -> productService.create(productDto));

        // then
        assertThat(exception.getStatus()).isEqualTo(httpStatus);
    }

    @Test
    public void shouldFetchAllProductsSuccess() {
        // given
        ProductCategory category = ProductCategory.builder()
                .id(1L)
                .name("cat-1")
                .build();
        List<Product> products = List.of(
                Product.builder()
                        .id(1L)
                        .name("pro-1")
                        .description("product number 1")
                        .price(BigDecimal.valueOf(2.34))
                        .category(category)
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("pro-2")
                        .description("product number 2")
                        .price(BigDecimal.valueOf(3.45))
                        .category(category)
                        .build()
        );
        List<ProductDto> productDtos = List.of(
                ProductDto.builder()
                        .id(1L)
                        .name("pro-1")
                        .description("product number 1")
                        .price(BigDecimal.valueOf(2.34))
                        .categoryId(category.getId())
                        .build(),
                ProductDto.builder()
                        .id(2L)
                        .name("pro-2")
                        .description("product number 2")
                        .price(BigDecimal.valueOf(3.45))
                        .categoryId(category.getId())
                        .build()
        );
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(products)).thenReturn(productDtos);

        // when
        List<ProductDto> dtos = productService.fetchAll();

        // then
        assertThat(dtos.size()).isEqualTo(productDtos.size());
    }

    @Test
    public void shouldFindProductByIdSuccess() {
        // given
        long id = 1L;
        String categoryName = "cat-1";
        String proName = "pro-1";
        String desc = "Product number 1";
        BigDecimal price = BigDecimal.valueOf(3.45);
        ProductCategory category = ProductCategory.builder()
                .id(id)
                .name(categoryName)
                .build();
        Optional<Product> productOpt = Optional.of(Product.builder()
                .id(id)
                .name(proName)
                .description(desc)
                .price(price)
                .category(category)
                .build());
        ProductDto productDto = ProductDto.builder()
                .id(id)
                .name(proName)
                .description(desc)
                .price(price)
                .categoryId(id)
                .build();
        when(productRepository.findById(id)).thenReturn(productOpt);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // when
        ProductDto proDto = productService.fetchById(id);

        // then
        assertThat(proDto.getId()).isEqualTo(id);
        assertThat(proDto.getName()).isEqualTo(proName);
    }

    @Test
    public void shouldFetchByNullIdFail(){
        // given
        Long productId = null;
        String errorMsg = "Given id is null";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        when(productRepository.findById(productId)).thenThrow(IllegalArgumentException.class);

        // when
        AppBaseException exception = assertThrows(AppBaseException.class, ()-> productService.fetchById(productId));

        // then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
        assertThat(exception.getStatus()).isEqualTo(httpStatus);
    }

    @Test
    public void shouldFetchByIdNotFoundFail(){
        // given
        long productId = 1L;
        String errorMsg = "Product with id:" + productId + " not found";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // when
        AppBaseException exception = assertThrows(AppBaseException.class, () -> productService.fetchById(productId));

        // then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
        assertThat(exception.getStatus()).isEqualTo(httpStatus);
    }

    @Test
    public void shouldUpdateProductSuccess() {
        // given
        long productId = 1L;
        long categoryId = 1L;
        String categoryName = "cat-1";

        ProductCategory category = ProductCategory.builder()
                .id(categoryId)
                .name(categoryName)
                .build();
        ProductDto productRequest = ProductDto.builder()
                .name("pro-2")
                .description("desc-2")
                .price(BigDecimal.valueOf(2.34))
                .categoryId(categoryId)
                .build();
        Optional<Product> existingProductOpt = Optional.of(Product.builder()
                .id(productId)
                .name("pro-1")
                .description("desc-1")
                .price(BigDecimal.valueOf(1.23))
                .category(category)
                .build());
        Product newProduct = Product.builder()
                .id(productId)
                .name("pro-2")
                .description("desc-2")
                .price(BigDecimal.valueOf(2.34))
                .category(category)
                .build();
        ProductDto newProductDto = ProductDto.builder()
                .id(productId)
                .name("pro-2")
                .description("desc-2")
                .price(BigDecimal.valueOf(2.34))
                .categoryId(categoryId)
                .build();

        when(productRepository.findById(productId)).thenReturn(existingProductOpt);
        when(productCategoryService.findById(productRequest.getCategoryId())).thenReturn(category);
        when(productMapper.toEntity(productRequest, category)).thenReturn(newProduct);
        when(productRepository.save(newProduct)).thenReturn(newProduct);
        when(productMapper.toDto(newProduct)).thenReturn(newProductDto);

        // when
        ProductDto pro = productService.updateProduct(productId, productRequest);

        // then
        assertThat(pro.getId()).isEqualTo(productId);
        assertThat(pro.getName()).isEqualTo(productRequest.getName());
        assertThat(pro.getDescription()).isEqualTo(productRequest.getDescription());
        assertThat(pro.getPrice()).isEqualTo(productRequest.getPrice());
    }

    @Test
    public void shouldDeleteByIdSuccess(){
        // given
        long productId = 1L;
        doNothing().when(productRepository).deleteById(productId);

        // when
        productService.deleteById(productId);

        // then
        verify(productRepository,times(1)).deleteById(productId);
    }

    @Test
    public void shouldDeleteByNullIdFail(){
        // given
        Long productId = null;
        doThrow(IllegalArgumentException.class)
                .when(productRepository).deleteById(productId);
        String errorMsg = "Given id is null";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        // when
        AppBaseException exception = assertThrows(BadRequestException.class, ()-> productService.deleteById(productId));

        // then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
        assertThat(exception.getStatus()).isEqualTo(httpStatus);
    }

    @Test
    public void shouldCalculateTotalValueSuccess(){
        // given
        BigDecimal total = BigDecimal.valueOf(3.45);
        when(productRepository.totalProductPrice()).thenReturn(total);

        // when
        BigDecimal totalValue = productService.calculateTotalValue();

        // then
        assertThat(totalValue).isEqualTo(total);
    }
}