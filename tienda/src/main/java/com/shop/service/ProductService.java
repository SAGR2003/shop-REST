package com.shop.service;

import com.shop.controller.dto.ProductDTO;
import com.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProduct{
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
    public ProductDTO createProduct(ProductDTO product) {
        return productRepository.save(product);
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productUpdate) {
        return productRepository.save(productUpdate);
    }

    @Override
    public void deleteProductByCode(int code) {
        productRepository.deleteById(code);
    }
}
