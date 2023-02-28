package com.shop.service;

import com.shop.controller.dto.ProductDTO;
import com.shop.model.ShoppingCart;

public interface ICart {
    ShoppingCart getCartById(int id);
    String createCart(ShoppingCart cart);
    String addToCart(int id, int quantity, ProductDTO product);
    String removeFromCart(ShoppingCart cart, int code);
    String makeSale();
}
