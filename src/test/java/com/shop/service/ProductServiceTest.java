package com.shop.service;

import com.shop.model.Product;
import com.shop.repository.ProductRepository;
import com.shop.service.exception.ProductAlreadyExistsException;
import com.shop.service.exception.ProductNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @Test
    void When_getAllProducts_Then_return_ListProduct() {
        List<Product> expectedProducts = Arrays.asList(new Product(1, "Gansito", 1000, 3, new Date(System.currentTimeMillis())), new Product(2, "Chocoramo", 2400, 5, new Date(System.currentTimeMillis())), new Product(3, "Ponky", 1000, 10, new Date(System.currentTimeMillis())));
        Mockito.when(productRepository.findAll()).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.getAllProducts();

        Assertions.assertEquals(expectedProducts, actualProducts);
        Mockito.verify(productRepository).findAll();
    }

    @Test
    void Given_existing_code_When_getProductByCode_Then_return_Product() {
        int existingCode = 1;
        Product expectedProduct = new Product(1, "Gansito", 1000, 3, new Date(System.currentTimeMillis()));
        Mockito.when(productRepository.existsById(existingCode)).thenReturn(true);
        Mockito.when(productRepository.getById(existingCode)).thenReturn(expectedProduct);

        Product actualProduct = productService.getProductByCode(existingCode);

        Assertions.assertEquals(existingCode, actualProduct.getCode());
        Assertions.assertEquals(expectedProduct, actualProduct);
        Mockito.verify(productRepository).existsById(existingCode);
        Mockito.verify(productRepository).getById(existingCode);
    }

    @Test
    void Given_invalid_code_When_getProductByCode_Then_throw_ProductNotFoundException() {
        int invalidCode = 2;
        Mockito.when(productRepository.existsById(invalidCode)).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductByCode(invalidCode));
        Mockito.verify(productRepository).existsById(invalidCode);
    }

    @Test
    void Given_product_with_unexisiting_code_When_createProduct_Then_create_product_successfully() {
        Product product = new Product(1, "Gansito", 1000, 3, null);
        Mockito.when(productRepository.existsById(product.getCode())).thenReturn(false);

        String response = productService.createProduct(product);

        Assertions.assertEquals("The product was created successfully", response);
        Mockito.verify(productRepository).save(product);
        Mockito.verify(productRepository).existsById(product.getCode());
    }

    @Test
    void Given_product_with_exisiting_code_When_createProduct_Then_throw_ProductAlreadyExistsException() {
        Product product = new Product(1, "Gansito", 1000, 3, null);
        Mockito.when(productRepository.existsById(product.getCode())).thenReturn(true);

        Assertions.assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(product));
        Mockito.verify(productRepository).existsById(product.getCode());
    }

    @Test
    void Given_existing_product_code_When_updateProduct_Then_update_product_successfully() {
        int existingCode = 1;
        Product productToUpdate = new Product(existingCode, "Gansito", 1000, 3, new Date(System.currentTimeMillis()));
        Mockito.when(productRepository.existsById(existingCode)).thenReturn(true);
        Mockito.when(productRepository.getById(existingCode)).thenReturn(productToUpdate);

        String response = productService.updateProduct(existingCode, 3);

        Assertions.assertEquals(6, productToUpdate.getStock());
        Assertions.assertEquals("The product stock was updated successfully", response);

        Mockito.verify(productRepository).existsById(existingCode);
        Mockito.verify(productRepository).getById(existingCode);
        Mockito.verify(productRepository).save(productToUpdate);
    }

    @Test
    void Given_unexisting_product_code_When_updateProduct_Then_throw_ProductNotFoundException() {
        int unexistingCode = 1;
        Mockito.when(productRepository.existsById(unexistingCode)).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(unexistingCode, 5));
        Mockito.verify(productRepository).existsById(unexistingCode);
    }
}