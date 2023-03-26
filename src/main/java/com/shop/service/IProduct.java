package com.shop.service;

import com.shop.model.Product;

import java.util.List;

public interface IProduct {
    List<Product> getAllProducts();

    Product getProductByCode(int code);

    String createProduct(Product product);

    String updateProduct(int productCode, int quantity);
}
