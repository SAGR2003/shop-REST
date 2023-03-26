package com.shop.service.exception;

public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException(int document) {
        super("The client with the document " + document + " never bought anything");
    }
}
