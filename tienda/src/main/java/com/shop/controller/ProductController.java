package com.shop.controller;

import com.shop.controller.dto.ProductDTO;
import com.shop.service.ProductService;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Operation(summary = "Get all products")
    @GetMapping(path = "/all")
    private ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    @Operation(summary = "Get product by code")
    @GetMapping(path = "/{code}")
    private ResponseEntity<ProductDTO> getProduct(@PathVariable int code) {
        ProductDTO product = productService.getProductByCode(code);
        return ResponseEntity.ok(product);
    }
    @Operation(summary = "Create product")
    @PostMapping(path = "/create")
    private ResponseEntity<String> createProduct(@RequestBody ProductDTO product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }
    @Operation(summary = "Update product by code")
    @PutMapping(path = "/update/{code}")
    private ResponseEntity<String> updateProduct(@PathVariable int code, @RequestBody ProductDTO newProduct) {
        newProduct.setCode(code);
        return ResponseEntity.ok(productService.updateProduct(newProduct));
    }
    @Operation(summary = "Delete product by code")
    @DeleteMapping(path = "/delete/{code}")
    private ResponseEntity<String> deleteProduct(@PathVariable int code) {
        return ResponseEntity.ok(productService.deleteProductByCode(code));
    }
    @Operation(summary = "Create example products")
    @PostMapping(path = "/createExampleProducts")
    private ResponseEntity<String> createExampleProducts() {
        return ResponseEntity.ok(productService.createExampleProducts());
    }
}
