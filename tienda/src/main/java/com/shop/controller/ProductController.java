package com.shop.controller;

import com.shop.controller.dto.ProductDTO;
import com.shop.service.IProduct;
import com.shop.service.ProductService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping(path = "/products/all")
    private ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping(path = "/product/{code}")
    private ResponseEntity<ProductDTO> getProduct(@PathVariable int code){
        ProductDTO product = productService.getProductByCode(code);
        return ResponseEntity.ok(product);
    }

    @PostMapping(path = "/product/create")
    private ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product){
        ProductDTO newProduct = productService.createProduct(product);
        return ResponseEntity.ok(newProduct);
    }
}
