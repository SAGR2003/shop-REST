package com.shop.controller;

import com.shop.model.ShoppingCart;
import com.shop.service.CartService;
import com.shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class ShoppingCartController {
    private CartService cartService;
    private ProductService productService;
    @Operation(summary = "Get shopping cart by id")
    @GetMapping(path = "/{idCart}")
    private ShoppingCart getCart(@PathVariable int idCart) {
        return cartService.getCartById(idCart);
    }
    @Operation(summary = "Create shopping cart")
    @PostMapping(path = "")
    private String createCart(@RequestBody ShoppingCart shoppingCart) {
        return cartService.createCart(shoppingCart);
    }
    @Operation(summary = "Add item to shopping cart")
    @PutMapping(path = "/{idCart}/{codeProduct}/{quantity}")
    private String addProduct(@PathVariable int idCart, @PathVariable int codeProduct, @PathVariable int quantity) {
        return cartService.addToCart(idCart, quantity, productService.getProductByCode(codeProduct));
    }
    @Operation(summary = "Remove product by id from shopping cart")
    @PutMapping(path = "/{idCart}/{codeProduct}")
    private String removeProduct(@PathVariable int idCart, @PathVariable int codeProduct) {
        return cartService.removeFromCart(cartService.getCartById(idCart), codeProduct);
    }

    @Operation(summary = "Sell products from a specific cart")
    @GetMapping(path = "/{idCart}/sell")
    private String makeSell(@PathVariable int idCart) {
        return cartService.makeSale(cartService.getCartById(idCart));
    }
}
