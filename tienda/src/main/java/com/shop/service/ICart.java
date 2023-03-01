package com.shop.service;

import com.shop.model.Product;
import com.shop.model.ShoppingCart;

public interface ICart {
    ShoppingCart getCartById(int id);
    String createCart(ShoppingCart cart);
    String addToCart(int id, int quantity, Product product);
    String removeFromCart(ShoppingCart cart, int code);
    String makeSale(ShoppingCart cart);
}
