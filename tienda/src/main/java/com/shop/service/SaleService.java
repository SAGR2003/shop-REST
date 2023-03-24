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
            sale = createSale(documentClient, cartItems);
            saleRepository.save(sale);
            saveCartItems(sale.getId(), cartItems);
            message = createBill(sale);
        }
        return message;
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
            Product product = productRepository.findById(item.getProductCode()).orElse(null);
            if (null != product) {
                productsSold.append(item.getQuantity()).append("x ").append(product.getName()).append(" / ");
            }
        }
        productsSold.append("Total = ").append(sale.getTotalAmount());
        return productsSold.toString();
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

    private boolean allProductsExists(List<CartItem> cartItems) {
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
