package com.shop.service;

import com.shop.model.Product;
import com.shop.model.CartItem;
import com.shop.model.ShoppingCart;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CartService implements ICart {
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;

    @Override
    public ShoppingCart getCartById(int id) {
        ShoppingCart shoppingCart = new ShoppingCart();
        if (cartRepository.existsById(id)) {
            shoppingCart = cartRepository.findById(id).get();
        }
        return shoppingCart;
    }

    @Override
    public String createCart(ShoppingCart cart) {
        String message = "That id already exists";
        if (!cartExists(cart.getId())) {
            cartRepository.save(cart);
            message = "The shopping cart was created successfully";
        }
        return message;
    }

    @Override
    public String addToCart(int idFromCart, int quantity, Product product) {
        ShoppingCart cart;
        String error = validationsAddToCart(product, idFromCart, quantity);
        String message = error;
        if (null == error) {
            cart = cartRepository.getById(idFromCart);
            CartItem cartItem = updateCartItem(cart, product, quantity);
            cart.getCartItems().add(cartItem);
            message = "The product was added successfully";
            cartRepository.save(cart);
        }
        return message;
    }

    @Override
    public String removeFromCart(ShoppingCart cart, int code) {
        CartItem itemToRemove = findItemByProductCode(cart, code);
        String error = validationsRemoveFromCart(cart, itemToRemove);
        String message = error;
        if (null == error) {
            cart.cartItems.remove(itemToRemove);
            cartRepository.save(cart);
            message = "The product was removed successfully";
        }
        return message;
    }

    @Override
    public String makeSale(ShoppingCart cart) {
        List<CartItem> soldItems = cart.getCartItems();
        StringBuilder productsSold = new StringBuilder();
        Product product;

        if (null == soldItems){
            productsSold.append("There are no products on that cart");
            return productsSold.toString();
        }

        productsSold.append("Products sold: ");
        for (CartItem soldItem : soldItems) {
            product = productRepository.findById(soldItem.getProductCode()).get();
            int soldQuantity = soldItem.getQuantity();
            if (enoughStock(product, soldQuantity)) {
                updateStock(product, soldQuantity);
                productsSold.append(soldQuantity).append("x ").append(product.getName()).append(", ");
                cartRepository.delete(cart);
            } else {
                productsSold.setLength(0);
                productsSold.append("There is not enough stock for the product: " + product).append("  ");
            }
        }
        productsSold.setLength(productsSold.length() - 2);

        return productsSold.toString();
    }

    private void updateStock(Product product, int soldQuantity) {
        int currentStock = product.getStock();
        int newStock = currentStock - soldQuantity;
        product.setStock(newStock);
        productRepository.save(product);
    }

    private CartItem updateCartItem(ShoppingCart cart, Product product, int quantity) {
        CartItem cartItem;
        CartItem item;

        item = findItemByProductCode(cart, product.getCode());
        if (null != item) {
            item.setQuantity(item.getQuantity() + quantity);
            item.setAmount(item.getAmount() + quantity * product.getUnitValue());
            cartItemRepository.save(item);
            return item;
        } else {
            cartItem = convertProductToCartItem(product, quantity);
            return cartItem;
        }
    }

    private CartItem convertProductToCartItem(Product product, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setProductCode(product.getCode());
        cartItem.setProductName(product.getName());
        cartItem.setQuantity(quantity);
        cartItem.setAmount(product.getUnitValue() * quantity);
        cartItemRepository.save(cartItem);
        return cartItem;
    }

    private String validationsAddToCart(Product product, int idFromCart, int quantity) {
        String messageError = null;

        if (!productExists(product.getCode())) {
            messageError = "The product doesn't exist";
        } else if (!cartExists(idFromCart)) {
            messageError = "The cart doesn't exist";
        } else if (!enoughStock(product, quantity)) {
            messageError = "There is not enough stock for the product";
        }
        return messageError;
    }

    private String validationsRemoveFromCart(ShoppingCart cart, CartItem itemToRemove) {
        String messageError = null;

        if (!cartExists(cart.getId())) {
            messageError = "You are trying to use a cart that doesn't exist";
        } else if (!cartContainsItem(cart, itemToRemove)) {
            messageError = "You are trying to remove a product that isn't at your cart";
        }
        return messageError;
    }

    private CartItem findItemByProductCode(ShoppingCart cart, int productCode) {
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem item : cartItems) {
            if (item.getProductCode() == productCode) {
                return item;
            }
        }
        return null;
    }

    public boolean productExists(int productCode) {
        return productRepository.existsById(productCode);
    }

    private boolean cartExists(int cartId) {
        return cartRepository.existsById(cartId);
    }

    private boolean enoughStock(Product product, int quantity) {
        return product.getStock() >= quantity;
    }

    private boolean cartContainsItem(ShoppingCart cart, CartItem item) {
        return cart.getCartItems().contains(item);
    }
}