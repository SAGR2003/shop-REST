package com.shop.controller;

import com.shop.AbstractTest;
import com.shop.controller.dto.ListResponseDTO;
import com.shop.controller.dto.ProductResponseDTO;
import com.shop.controller.dto.ResponseDTO;
import com.shop.model.Product;
import com.shop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.shop.service.ProductService;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest extends AbstractTest {

    private static final String PATH_PRODUCT = "/products";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;


    @Test
    void When_getAllProducts_and_two_products_on_db_Then_return_ListResponseDTO_with_two_products() {
        List<Product> products = createAndSaveTwoProducts();

        ResponseEntity<ListResponseDTO> response = restTemplate.getForEntity(PATH_PRODUCT, ListResponseDTO.class);

        assertEquals(2, response.getBody().getListResponse().size());
        assertEquals("{code=1, name=Tampico, unitValue=1100, stock=5, dateCreated=" + products.get(0).getDateCreated() + "}", String.valueOf(response.getBody().getListResponse().get(0)));
        assertEquals("{code=2, name=Gansito, unitValue=1000, stock=10, dateCreated=" + products.get(1).getDateCreated() + "}", String.valueOf(response.getBody().getListResponse().get(1)));
    }

    @Test
    void Given_existing_productCode_When_getProduct_Then_return_ProductResponseDTO() {
        int productCode = 1;
        String productName = "Tampico";
        int unitValue = 1100;
        int stock = 5;

        Product product = new Product(productCode, productName, unitValue, stock, new Date(System.currentTimeMillis()));
        productRepository.save(product);

        ResponseEntity<ProductResponseDTO> response = restTemplate.getForEntity(PATH_PRODUCT + "/{code}", ProductResponseDTO.class, productCode);

        assertEquals(productCode, response.getBody().getCode());
        assertEquals(productName, response.getBody().getName());
        assertEquals(unitValue, response.getBody().getUnitValue());
        assertEquals(stock, response.getBody().getStock());
    }

    @Test
    void Given_existing_productCode_When_updateProduct_Then_return_successMessage() {
        List<Product> products = createAndSaveTwoProducts();
        int quantityToAdd = 3;

        ResponseEntity<ResponseDTO> response = restTemplate.exchange(PATH_PRODUCT + "/{code}/{quantityToAdd}", HttpMethod.PUT, null, ResponseDTO.class, products.get(0).getCode(), quantityToAdd);

        assertEquals("The product stock was updated successfully", response.getBody().getResponse());

        Product updatedProduct = productRepository.findById(products.get(0).getCode()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(products.get(0).getName(), updatedProduct.getName());
        assertEquals(products.get(0).getUnitValue(), updatedProduct.getUnitValue());
        assertEquals(products.get(0).getStock() + quantityToAdd, updatedProduct.getStock());
    }

    private List<Product> createAndSaveTwoProducts() {
        Product product1 = new Product(1, "Tampico", 1100, 5, new Date(System.currentTimeMillis()));
        Product product2 = new Product(2, "Gansito", 1000, 10, new Date(System.currentTimeMillis()));
        productRepository.save(product1);
        productRepository.save(product2);
        return Arrays.asList(product1, product2);
    }

    @Test
    void Given_product_When_createProduct_Then_product_is_created_successfully() {
        productRepository.deleteAll();
        Product product = new Product(17, "Gansito", 1000, 3, new Date(System.currentTimeMillis()));

        ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(PATH_PRODUCT, product, ResponseDTO.class);
        assertEquals("The product was created successfully", response.getBody().getResponse());

        List<Product> products = productService.getAllProducts();
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }
}