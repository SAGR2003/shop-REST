package com.shop.controller;

import com.shop.controller.dto.ListResponseDTO;
import com.shop.controller.dto.ProductResponseDTO;
import com.shop.controller.dto.ResponseDTO;
import com.shop.model.Product;
import com.shop.service.IProduct;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Tag(name = "product-controller", description = "Manage Product items")
public class ProductController {
    private IProduct productService;

    @Operation(summary = "Get all products")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Successfull"), @ApiResponse(code = 500, message = "Unexpected error.")})
    @GetMapping(path = "")
    private ListResponseDTO getAllProducts() {
        return new ListResponseDTO(productService.getAllProducts());
    }

    @Operation(summary = "Get product by code")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Successfull"), @ApiResponse(code = 500, message = "Unexpected error. The product code does not exist"), @ApiResponse(code = 400, message = "Bad request. The product code must be integer and greater than 0")})
    @GetMapping(path = "/{code}")
    private ProductResponseDTO getProduct(@PathVariable int code) {
        Product productByCode = productService.getProductByCode(code);
        return new ProductResponseDTO(
                productByCode.getCode(),
                productByCode.getName(),
                productByCode.getUnitValue(),
                productByCode.getStock(),
                productByCode.getDateCreated()
        );
    }

    @Operation(summary = "Create product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Successfull"), @ApiResponse(code = 500, message = "Unexpected error. This product code already exists"), @ApiResponse(code = 400, message = "Bad request. The product code and unit value must be integer and greater than 0")})
    @PostMapping(path = "")
    private ResponseDTO createProduct(@RequestBody Product product) {
        return new ResponseDTO(productService.createProduct(product));
    }

    @Operation(summary = "Update stock product by code")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Successfull"), @ApiResponse(code = 500, message = "Unexpected error. The product code does not exist"), @ApiResponse(code = 400, message = "Bad request. The quantity to add must be integer and greater than 0")})
    @PutMapping(path = "/{code}/{quantityToAdd}")
    private ResponseDTO updateProduct(@PathVariable int code, @PathVariable int quantityToAdd) {
        return new ResponseDTO(productService.updateProduct(code, quantityToAdd));
    }
}
