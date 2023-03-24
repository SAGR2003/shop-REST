package com.shop.service.exception;

public class ProductAlreadyExistsException extends RuntimeException{
    public ProductAlreadyExistsException(int code) {
        super("Product with code " + code + " already exists");
    }
}
