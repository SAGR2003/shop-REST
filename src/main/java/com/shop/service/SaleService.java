package com.shop.service;

import com.shop.model.AsyncSale;
import com.shop.model.CartItem;
import com.shop.model.Product;
import com.shop.model.Sale;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.SaleRepository;
import com.shop.service.exception.ClientNotFoundException;
import com.shop.service.exception.DailyTransactionLimitExceededException;
import com.shop.service.exception.InsufficientStockException;
import com.shop.service.exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class SaleService implements ISale {
    private SaleRepository saleRepository;
    private ProductRepository productRepository;
    private CartItemRepository cartItemRepository;

    private Date todaysDate() {
        return new Date(System.currentTimeMillis());
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public List<Sale> getSalesByDocument(int document) {
        if (!saleRepository.findAllByDocumentClient(document).isEmpty()) {
            return saleRepository.findAllByDocumentClient(document);
        } else {
            throw new ClientNotFoundException(document);
        }
    }

    @Override
    public String makeSale(int documentClient, List<CartItem> cartItems) {
        Sale sale;
        validationsToSell(documentClient, cartItems);
        sale = createSale(documentClient, cartItems);
        saleRepository.save(sale);
        saveCartItems(sale.getId(), cartItems);
        return createBill(sale);
    }

    @Override
    public String makeSaleAsync(AsyncSale shoppingCart) {
        String bill = makeSale(shoppingCart.getDocumentClient(), shoppingCart.getCartItems());
        String response = "The bill is: " + bill + " for the client with document: " + shoppingCart.getDocumentClient() + ", sending order to address: " + shoppingCart.getDestinationAddress();
        System.out.println(response);
        return response;
    }

    private Sale createSale(int documentClient, List<CartItem> cartItems) {
        Sale sale = new Sale();
        sale.setDocumentClient(documentClient);
        sale.setDateCreated(todaysDate());
        sale.setCartItems(cartItems);
        sale.setTotalAmount(sellProducts(sale));
        return sale;
    }

    private void saveCartItems(int saleId, List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            item.setSaleId(saleId);
            cartItemRepository.save(item);
        }
    }

    private int sellProducts(Sale sale) {
        int totalAmount = 0;
        for (CartItem item : sale.getCartItems()) {
            Product product = productRepository.getById(item.getProductCode());
            totalAmount += product.getUnitValue() * item.getQuantity();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
        return totalAmount;
    }

    private String createBill(Sale sale) {
        StringBuilder productsSold = new StringBuilder();
        productsSold.append("I sell ");
        for (CartItem item : sale.getCartItems()) {
            Product product = productRepository.getById(item.getProductCode());
            productsSold.append(item.getQuantity()).append("x ").append(product.getName()).append(" / ");
        }
        productsSold.append("Total = ").append(sale.getTotalAmount());
        return productsSold.toString();
    }

    private void validationsToSell(int document, List<CartItem> cartItems) {
        validateDailyTransactionLimit(document);
        validateAllProductsExist(cartItems);
        validateEnoughStock(cartItems);
    }

    private void validateDailyTransactionLimit(int document) {
        int todaysTransactionsByDocument = saleRepository.countByDocumentClientAndDateCreated(document, todaysDate());
        if (todaysTransactionsByDocument >= 93) {
            throw new DailyTransactionLimitExceededException(document);
        }
    }

    private void validateAllProductsExist(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            if (!productRepository.existsById(item.getProductCode())) {
                throw new ProductNotFoundException(item.getProductCode());
            }
        }
    }

    private void validateEnoughStock(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Product product = productRepository.getById(item.getProductCode());
            if (product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException(product.getName());
            }
        }
    }
}