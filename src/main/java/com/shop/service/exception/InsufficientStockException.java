package com.shop.service.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String product) {
        super("There is not enough stock for the product " + product);
    }
}
