package com.shop.controller;

import com.shop.controller.dto.ProductDTO;
import com.shop.service.ProductService;
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
    private ResponseEntity<ProductDTO> getProduct(@PathVariable int code) {
        ProductDTO product = productService.getProductByCode(code);
        return ResponseEntity.ok(product);
    }

    @PostMapping(path = "/product/create")
    private ResponseEntity<String> createProduct(@RequestBody ProductDTO product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PutMapping(path = "/product/update/{code}")
    private ResponseEntity<String> updateProduct(@PathVariable int code, @RequestBody ProductDTO newProduct) {
        newProduct.setCode(code);
        return ResponseEntity.ok(productService.updateProduct(newProduct));
    }

    @DeleteMapping(path = "/product/delete/{code}")
    private ResponseEntity<String> deleteProduct(@PathVariable int code) {
        return ResponseEntity.ok(productService.deleteProductByCode(code));
    }
}
