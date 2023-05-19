package com.shop.service;

import com.shop.model.AsyncSale;
import com.shop.model.CartItem;
import com.shop.model.Sale;

import java.util.List;

public interface ISale {
    List<Sale> getAllSales();

    List<Sale> getSalesByDocument(int document);

    String makeSale(int documentClient, List<CartItem> cartItems);

    String makeSaleAsync(AsyncSale shoppingCart);
}
