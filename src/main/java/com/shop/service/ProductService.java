package com.shop.service;

import com.shop.model.Product;
import com.shop.repository.ProductRepository;
import com.shop.service.exception.ProductAlreadyExistsException;
import com.shop.service.exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService implements IProduct {
    private ProductRepository productRepository;

    private Date todaysDate() {
        return new Date(System.currentTimeMillis());
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductByCode(int code) {
        if (productRepository.existsById(code)) {
            return productRepository.getById(code);
        } else {
            throw new ProductNotFoundException(code);
        }
    }

    @Override
    public String createProduct(Product product) {
        if (!productRepository.existsById(product.getCode())) {
            product.setDateCreated(todaysDate());
            productRepository.save(product);
            return "The product was created successfully";
        } else {
            throw new ProductAlreadyExistsException(product.getCode());
        }
    }

    @Override
    public String updateProduct(int productCode, int quantity) {
        Product productToUpdate = getProductByCode(productCode);
        productToUpdate.setStock(productToUpdate.getStock() + quantity);
        productRepository.save(productToUpdate);
        return "The product stock was updated successfully";
    }
}
