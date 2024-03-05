package edu.gafur.inventoryservice.service;

import edu.gafur.inventoryservice.domain.ProductCategory;
import edu.gafur.inventoryservice.exception.base.AppBaseException;
import edu.gafur.inventoryservice.repository.ProductCategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductCategoryServiceTest extends BaseTestCase{

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductCategoryService productCategoryService;

    @Test
    public void testCreateNewProductCategorySuccess(){
        // given
        ProductCategory productCategory = ProductCategory.builder()
                .name("cat-1")
                .build();
        ProductCategory savedCategory = ProductCategory.builder()
                .id(1L)
                .name(productCategory.getName())
                .build();
        when(productCategoryRepository.save(productCategory)).thenReturn(savedCategory);

        // when
        ProductCategory createdCategory = productCategoryService.create(productCategory);

        // then
        assertThat(createdCategory.getId()).isEqualTo(1L);
        assertThat(createdCategory.getName()).isEqualTo("cat-1");
    }

    @Test
    public void testCreateNullProductCategoryFail(){
        // given
        when(productCategoryRepository.save(any())).thenThrow(new IllegalArgumentException());

        // when
        AppBaseException exception = assertThrows(AppBaseException.class, () -> productCategoryService.create(null));

        // then
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testCreateNewProductCategoryNameIsNullFail(){
        // given
        ProductCategory productCategory = ProductCategory.builder()
                .name(null)
                .build();
        when(productCategoryRepository.save(productCategory)).thenThrow(new IllegalArgumentException());

        // when
        AppBaseException exception = assertThrows(AppBaseException.class, () -> productCategoryService.create(productCategory));

        // then
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testCreateNewDuplicatedNameProductCategoryFail(){
        // given
        ProductCategory productCategory = ProductCategory.builder()
                .name("cat-1")
                .build();
        ProductCategory savedCategory = ProductCategory.builder()
                .id(1L)
                .name(productCategory.getName())
                .build();
        String errorMsg = "duplicate key value violates unique constraint";
        when(productCategoryRepository.save(productCategory))
                .thenReturn(savedCategory)
                .thenThrow(new DataIntegrityViolationException(errorMsg));

        // when
        AppBaseException exception = assertThrows(AppBaseException.class, () -> {
            productCategoryService.create(productCategory);
            productCategoryService.create(productCategory);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(errorMsg);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testFindByIdSuccess(){
        // given
        ProductCategory productCategory = ProductCategory.builder()
                .id(1L)
                .name("cat-1")
                .build();
        Optional<ProductCategory> productCategoryOptional = Optional.of(productCategory);

        when(productCategoryRepository.findById(1L)).thenReturn(productCategoryOptional);

        // when
        ProductCategory existingCategory = productCategoryService.findById(1L);

        // then
        assertThat(existingCategory.getId()).isEqualTo(productCategory.getId());
        assertThat(existingCategory.getName()).isEqualTo(productCategory.getName());
    }

    @Test
    public void testFindProductCategoryNullIdFail(){
        // given
        String msg = "Given id is null";
        when(productCategoryRepository.findById(any())).thenThrow(new IllegalArgumentException());

        // when
        AppBaseException exception = assertThrows(AppBaseException.class, () -> productCategoryService.findById(null));

        // then
        assertThat(exception.getMessage()).isEqualTo(msg);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testFindProductCategoryNotFoundFail(){
        // given
        String msg = "Category not found";
        when(productCategoryRepository.findById(any())).thenReturn(Optional.empty());

        // when
        AppBaseException exception = assertThrows(AppBaseException.class, () -> productCategoryService.findById(1L));

        // then
        assertThat(exception.getMessage()).isEqualTo(msg);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testFetchAllCategoriesSuccess(){
        // given
        List<ProductCategory> categories = List.of(
                ProductCategory.builder()
                        .id(1L)
                        .name("cat-1")
                        .build(),
                ProductCategory.builder()
                        .id(2L)
                        .name("cat-2")
                        .build()
        );

        when(productCategoryRepository.findAll()).thenReturn(categories);

        // when
        List<ProductCategory> existingCategories = productCategoryService.fetchAll();

        // then
        assertThat(existingCategories.size()).isEqualTo(categories.size());
    }
}