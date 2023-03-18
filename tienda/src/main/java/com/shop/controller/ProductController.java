package com.shop.controller;

import com.shop.model.Product;
import com.shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @Operation(summary = "Get all products")
    @GetMapping(path = "")
    private List<Product> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products;
    }

    @Operation(summary = "Get product by code")
    @GetMapping(path = "/{code}")
    private Product getProduct(@PathVariable int code) {
        Product product = productService.getProductByCode(code);
        return product;
    }

    @Operation(summary = "Create product")
    @PostMapping(path = "")
    private String createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @Operation(summary = "Update product by code")
    @PutMapping(path = "/{code}")
    private String updateProduct(@PathVariable int code, @RequestBody Product newProduct) {
        newProduct.setCode(code);
        return productService.updateProduct(newProduct);
    }

    @Operation(summary = "Delete product by code")
    @DeleteMapping(path = "/{code}")
    private String deleteProduct(@PathVariable int code) {
        return productService.deleteProductByCode(code);
    }

    @Operation(summary = "Create example products")
    @PostMapping(path = "/examples")
    private String createExampleProducts() {
        return productService.createExampleProducts();
    }
}
