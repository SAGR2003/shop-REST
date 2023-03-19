package com.shop.service;

import com.shop.model.Product;
import com.shop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Date;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService implements IProduct {
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductByCode(int code) {
        Product product = new Product();
        if (productRepository.existsById(code)) {
            product = productRepository.findById(code).get();
        }
        return product;
    }

    @Override
    public String createProduct(Product product) {
        String message = "That code already exists";
        if (!productRepository.existsById(product.getCode())) {
            product.setDateCreated(new Date(System.currentTimeMillis()));
            productRepository.save(product);
            message = "The product was created successfully";
        }
        return message;
    }

    @Override
    public String updateProduct(Product productUpdate) {
        String message = "You are trying to update a product that doesn't exist";
        if (productRepository.existsById(productUpdate.getCode())) {
            productUpdate.setDateCreated(new Date(System.currentTimeMillis()));

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
        if (getAllProducts().isEmpty()) {
            Date dateCreated = new Date(System.currentTimeMillis());
            productRepository.save(new Product(1, "Gansito", 1000, 3, dateCreated));
            productRepository.save(new Product(2, "Chocoramo", 2400, 5, dateCreated));
            productRepository.save(new Product(3, "Ponky", 1000, 10, dateCreated));
            message = "The example products were created successfully";
        }
        return message;
    }
}
