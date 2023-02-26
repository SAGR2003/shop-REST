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
    private ResponseEntity<ProductDTO> getProduct(@PathVariable int code){
        ProductDTO product = productService.getProductByCode(code);
        return ResponseEntity.ok(product);
    }

    @PostMapping(path = "/product/create")
    private ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product){
        ProductDTO newProduct = productService.createProduct(product);
        return ResponseEntity.ok(newProduct);
    }

    @PutMapping(path = "/product/update/{code}")
    private ResponseEntity<ProductDTO> updateProduct(@PathVariable int code, @RequestBody ProductDTO newProduct){
        newProduct.setCode(code);
        ProductDTO productUpdate = productService.updateProduct(newProduct);
        return ResponseEntity.ok(productUpdate);
    }
    @DeleteMapping (path = "/product/delete/{code}")
    private ResponseEntity<ProductDTO> deleteProduct(@PathVariable int code){
        productService.deleteProductByCode(code);
        return ResponseEntity.ok(null);
    }
}
