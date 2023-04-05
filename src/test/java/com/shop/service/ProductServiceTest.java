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
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void When_getAllProducts_Then_return_ProductList(){
        List<Product> testProducts = new ArrayList<Product>();
        testProducts.add(new Product(1,"Gelatina",1299,3,new Date(System.currentTimeMillis())));
        testProducts.add(new Product(2,"Coca Cola",2000,3,new Date(System.currentTimeMillis())));
        Mockito.when(productRepository.findAll()).thenReturn(testProducts);

        List<Product> actualProducts = productService.getAllProducts();

        Assertions.assertEquals(testProducts, actualProducts);
        Mockito.verify(productRepository).findAll();
    }
    @Test
    void Given_Product_existsByCode_When_getProductByCode_Then_return_Product() {
        Product testProduct  = new Product(1,"Gelatina",1299,3,new Date(System.currentTimeMillis()));
        Mockito.when(productRepository.existsById(testProduct.getCode())).thenReturn(true);
        Mockito.when(productRepository.getById(testProduct.getCode())).thenReturn(testProduct);

        Product result = productService.getProductByCode(testProduct.getCode());

        Assertions.assertEquals(testProduct,result);
        Mockito.verify(productRepository).existsById(testProduct.getCode());
        Mockito.verify(productRepository).getById(testProduct.getCode());
    }
    @Test
    public void Given_Product_not_existsByCode_When_getProductByCode_Then_Product_not_found(){
        Product testProduct  = new Product(1,"Gelatina",1299,3,new Date(System.currentTimeMillis()));
        Mockito.when(productRepository.existsById(testProduct.getCode())).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductByCode(testProduct.getCode());
        });
        Mockito.verify(productRepository).existsById(testProduct.getCode());
    }


    @Test
    public void Given_Product_does_not_exist_When_createProduct_Then_Product_is_created_successfully(){
        Product testProduct  = new Product(1,"Gelatina",1299,3,new Date(System.currentTimeMillis()));
        Mockito.when(productRepository.existsById(testProduct.getCode())).thenReturn(false);

        String message = productService.createProduct(testProduct);

        Assertions.assertEquals("The product was created successfully", message);
        Mockito.verify(productRepository).save(testProduct);
        Mockito.verify(productRepository).existsById(testProduct.getCode());
    }

    @Test
    public void Given_Product_exists_When_createProduct_Then_Product_already_exists(){
        Product testProduct  = new Product(1,"Gelatina",1299,3,new Date(System.currentTimeMillis()));
        Mockito.when(productRepository.existsById(testProduct.getCode())).thenReturn(true);

        Assertions.assertThrows(ProductAlreadyExistsException.class, () -> {
            productService.createProduct(testProduct);
        });

        Mockito.verify(productRepository).existsById(testProduct.getCode());
    }


}
