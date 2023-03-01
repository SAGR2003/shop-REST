package com.shop.service;

import com.shop.model.Product;
import com.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProduct {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductByCode(int code) {
        return productRepository.findById(code).get();
    }

    @Override
    public String createProduct(Product product) {
        String message = "That code already exists";
        if (!productRepository.existsById(product.getCode())) {
            productRepository.save(product);
            message = "The product was created successfully";
        }
        return message;
    }

    @Override
    public String updateProduct(Product productUpdate) {
        String message = "You are trying to update a product that doesn't exist";
        if (productRepository.existsById(productUpdate.getCode())) {
            productRepository.save(productUpdate);
            message = "The product was updated successfully";
        }
        return message;
    }

    @Override
    public String deleteProductByCode(int code) {
        String message = "You are trying to delete a product that doesn't exist";
        if (productRepository.existsById(code)) {
            productRepository.deleteById(code);
            message = "The product was deleted successfully";
        }
        return message;
    }

    public String createExampleProducts() {
        String message = "You already have some products";
        Product example1 = new Product(1, "Gansito", 1000, 3);
        Product example2 = new Product(2, "Chocoramo", 2400, 5);
        Product example3 = new Product(3, "Ponky", 1000, 10);
        if (getAllProducts().isEmpty()) {
            productRepository.save(example1);
            productRepository.save(example2);
            productRepository.save(example3);
            message = "The example products were created successfully";
        }
        return message;
    }
}
