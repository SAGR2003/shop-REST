package com.shop.service;

import com.shop.model.Product;
import com.shop.model.CartItem;
import com.shop.model.ShoppingCart;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService implements ICart {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ShoppingCart getCartById(int id) {
        return cartRepository.findById(id).get();
    }

    @Override
    public String createCart(ShoppingCart cart) {
        String message = "That id already exists";
        if (!cartRepository.existsById(cart.getId())) {
            cartRepository.save(cart);
            message = "The shopping cart was created successfully";
        }
        return message;
    }

    @Override
    public String addToCart(int idFromCart, int quantity, Product product) {
        ShoppingCart cart;
        CartItem cartItem = convertProductToCartItem(product, quantity);
        String message;

        cart = cartRepository.getById(idFromCart);
        cart.getCartItems().add(cartItem);
        message = "The product was added successfully";
        cartRepository.save(cart);

        return message;
    }

    @Override
    public String removeFromCart(ShoppingCart cart, int code) {
        CartItem itemToRemove = cartItemRepository.getById(code);
        String message = "You are trying to use a cart that doesn't exist";

        if (cartRepository.existsById(cart.getId())) {
            if (cart.getCartItems().contains(itemToRemove)) {
                cart.cartItems.remove(itemToRemove);
                cartRepository.save(cart);
                message = "The product was removed successfully";
            } else {
                message = "You are trying to remove a product that isn't at your cart";
            }
        }
        return message;
    }

    @Override
    public String makeSale(ShoppingCart cart) {
        List<CartItem> soldItems = cart.getCartItems();
        StringBuilder messageBuilder = new StringBuilder("Products sold: ");

        for (CartItem soldItem : soldItems) {
            Product product = productRepository.findById(soldItem.getProductCode()).get();
            int soldQuantity = soldItem.getQuantity();

            updateStock(product, soldQuantity);

            messageBuilder.append(soldQuantity);
            messageBuilder.append("x ");
            messageBuilder.append(product.getName());
            messageBuilder.append(", ");
        }
        messageBuilder.setLength(messageBuilder.length() - 2);

        return messageBuilder.toString();
    }

    private void updateStock(Product product, int soldQuantity) {
        int currentStock = product.getStock();
        int newStock = currentStock - soldQuantity;
        product.setStock(newStock);
        productRepository.save(product);
    }

    private CartItem convertProductToCartItem(Product product, int quantity) {
        CartItem cartItem = new CartItem(product.getCode(), product.getName(), quantity, product.getUnitValue());
        cartItemRepository.save(cartItem);
        return cartItem;
    }
}
