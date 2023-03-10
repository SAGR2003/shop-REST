package com.shop.controller;

import com.shop.model.ShoppingCart;
import com.shop.service.CartService;
import com.shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Get shopping cart by id")
    @GetMapping(path = "/{idCart}")
    private ResponseEntity<ShoppingCart> getCart(@PathVariable int idCart) {
        return ResponseEntity.ok(cartService.getCartById(idCart));
    }
    @Operation(summary = "Create shopping cart")
    @PostMapping(path = "")
    private ResponseEntity<String> createCart(@RequestBody ShoppingCart shoppingCart) {
        return ResponseEntity.ok(cartService.createCart(shoppingCart));
    }
    @Operation(summary = "Add item to shopping cart")
    @PutMapping(path = "/{idCart}/{codeProduct}/{quantity}")
    private ResponseEntity<String> addProduct(@PathVariable int idCart, @PathVariable int codeProduct, @PathVariable int quantity) {
        return ResponseEntity.ok(cartService.addToCart(idCart, quantity, productService.getProductByCode(codeProduct)));
    }
    @Operation(summary = "Remove product by id from shopping cart")
    @PutMapping(path = "/{idCart}/{codeProduct}")
    private ResponseEntity<String> removeProduct(@PathVariable int idCart, @PathVariable int codeProduct) {
        return ResponseEntity.ok(cartService.removeFromCart(cartService.getCartById(idCart), codeProduct));
    }

    @Operation(summary = "Sell products from a specific cart")
    @GetMapping(path = "/{idCart}/sell")
    private ResponseEntity<String> makeSell(@PathVariable int idCart) {
        return ResponseEntity.ok(cartService.makeSale(cartService.getCartById(idCart)));
    }
}
