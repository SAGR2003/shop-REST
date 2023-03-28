package com.shop.controller;

import com.shop.controller.dto.ListResponseDTO;
import com.shop.controller.dto.ProductResponseDTO;
import com.shop.controller.dto.ResponseDTO;
import com.shop.model.Product;
import com.shop.service.IProduct;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private IProduct productService;

    @Operation(summary = "Get all products")
    @GetMapping(path = "")
    private ListResponseDTO getAllProducts() {
        return new ListResponseDTO(productService.getAllProducts());
    }

    @Operation(summary = "Get product by code")
    @GetMapping(path = "/{code}")
    private ProductResponseDTO getProduct(@PathVariable int code) {
        return new ProductResponseDTO(productService.getProductByCode(code));
    }

    @Operation(summary = "Create product")
    @PostMapping(path = "")
    private ResponseDTO createProduct(@RequestBody Product product) {
        return new ResponseDTO(productService.createProduct(product));
    }

    @Operation(summary = "Update stock product by code")
    @PutMapping(path = "/{code}/{quantityToAdd}")
    private ResponseDTO updateProduct(@PathVariable int code, @PathVariable int quantityToAdd) {
        return new ResponseDTO(productService.updateProduct(code, quantityToAdd));
    }
}
