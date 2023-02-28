package com.shop.controller;

import com.shop.model.ShoppingCart;
import com.shop.service.CartService;
import com.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class ShoppingCartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;

    @GetMapping(path = "/{id}")
    private ResponseEntity<ShoppingCart> getCart(@PathVariable int id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PostMapping(path = "/create")
    private ResponseEntity<String> createCart(@RequestBody ShoppingCart shoppingCart) {
        return ResponseEntity.ok(cartService.createCart(shoppingCart));
    }

    @PutMapping(path = "/{id}/add/{code}/{quantity}")
    private ResponseEntity<String> addProduct(@PathVariable int id, @PathVariable int code, @PathVariable int quantity) {
        return ResponseEntity.ok(cartService.addToCart(id, quantity, productService.getProductByCode(code)));
    }

    @PutMapping(path = "/{id}/remove/{code}")
    private ResponseEntity<String> removeProduct(@PathVariable int id, @PathVariable int code) {
        return ResponseEntity.ok(cartService.removeFromCart(cartService.getCartById(id), code));
    }
}
