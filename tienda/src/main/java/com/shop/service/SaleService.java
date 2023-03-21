package com.shop.service;

import com.shop.model.CartItem;
import com.shop.model.Product;
import com.shop.model.Sale;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@AllArgsConstructor
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
        return saleRepository.findAllByDocumentClient(document);
    }

    @Override
    public String makeSale(int documentClient, List<CartItem> cartItems) {
        Sale sale;
        String message = validationsToSell(documentClient, cartItems);
        if (null == message) {
            sale = createSale(documentClient, sellCartItems(cartItems));
            saveCartItems(cartItems);
            saleRepository.save(sale);
            message = createBill(cartItems, sale.getTotalAmount());
        }
        return message;
    }

    private String createBill(List<CartItem> cartItems, int total) {
        StringBuilder productsSold = new StringBuilder();
        productsSold.append("I sell ");
        for (CartItem item : cartItems) {
            productsSold.append(item.getQuantity()).append("x ").append(productRepository.getById(item.getProductCode()).getName()).append(" / ");
        }
        productsSold.append("Total = ").append(total);
        return productsSold.toString();
    }

    private int sellCartItems(List<CartItem> cartItems) {
        int totalAmount = 0;
        Product product;

        for (CartItem item : cartItems) {
            product = productRepository.getById(item.getProductCode());
            updateStock(product, item.getQuantity());
            totalAmount += item.getQuantity() * product.getUnitValue();
        }
        return totalAmount;
    }

    private Sale createSale(int documentClient, int totalAmount) {
        Sale sale = new Sale();
        sale.setDocumentClient(documentClient);
        sale.setTotalAmount(totalAmount);
        sale.setDateCreated(todaysDate());
        return sale;
    }

    private void saveCartItems(List<CartItem> cartItems) {
        for (CartItem newCartItem : cartItems) {
            cartItemRepository.save(newCartItem);
        }
    }

    private void updateStock(Product product, int soldQuantity) {
        int currentStock = product.getStock();
        int newStock = currentStock - soldQuantity;
        product.setStock(newStock);
        productRepository.save(product);
    }

    private String validationsToSell(int document, List<CartItem> cartItems) {
        String messageError = null;
        String product = thereIsEnoughStock(cartItems);
        int todaysTransactionsByDocument = saleRepository.countByDocumentClientAndDateCreated(document, todaysDate());

        if (todaysTransactionsByDocument >= 3) {
            messageError = "You can't make more transactions for today, remember there are 3 per day";
        } else if (!allProductsExists(cartItems)) {
            messageError = "One or more of the products you're trying to buy doesn't exist";
        } else if (null != product) {
            messageError = "There is not enough stock for the product " + product;
        }
        return messageError;
    }

    public boolean allProductsExists(List<CartItem> cartItems) {
        boolean theyExist = false;
        for (CartItem item : cartItems) {
            theyExist = productRepository.existsById(item.getProductCode());
        }
        return theyExist;
    }

    private String thereIsEnoughStock(List<CartItem> cartItems) {
        String productOutOfStock = null;
        for (CartItem item : cartItems) {
            Product product = productRepository.getById(item.getProductCode());
            if (product.getStock() < item.getQuantity()) {
                productOutOfStock = product.getName();
            }
        }
        return productOutOfStock;
    }
}
