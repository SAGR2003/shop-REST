package com.shop.service;

import com.shop.controller.dto.ProductDTO;

import java.util.List;

public interface IProduct {
    List<ProductDTO> getAllProducts();

    ProductDTO getProductByCode(int code);

    String createProduct(ProductDTO product);

    String updateProduct(ProductDTO productUpdate);

    String deleteProductByCode(int code);
}
