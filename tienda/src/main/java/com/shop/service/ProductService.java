package com.shop.service;

import com.shop.controller.dto.ProductDTO;
import com.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProduct {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductDTO getProductByCode(int code) {
        return productRepository.findById(code).get();
    }

    @Override
    public String createProduct(ProductDTO product) {
        String message = "That code already exists";
        if (!productRepository.existsById(product.getCode())) {
            productRepository.save(product);
            message = "The product was created successfully";
        }
        return message;
    }

    @Override
    public String updateProduct(ProductDTO productUpdate) {
        String message = "You are trying to update a product that doesn't exists";
        if (productRepository.existsById(productUpdate.getCode())) {
            productRepository.save(productUpdate);
            message = "The product was updated successfully";
        }
        return message;
    }

    @Override
    public String deleteProductByCode(int code) {
        String message = "You are trying to delete a product that doesn't exists";
        if (productRepository.existsById(code)) {
            productRepository.deleteById(code);
            message = "The product was deleted successfully";
        }
        return message;
    }
}
