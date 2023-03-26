package com.shop.service.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(int productCode) {
        super("Product with code " + productCode + " was not found");
    }
}