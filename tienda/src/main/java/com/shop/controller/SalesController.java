package com.shop.controller;

import com.shop.model.CartItem;
import com.shop.model.Sale;
import com.shop.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@AllArgsConstructor
public class SalesController {
    private SaleService saleService;

    @Operation(summary = "Get all Sales")
    @GetMapping(path = "")
    private List<Sale> getAllTransactions() {
        return saleService.getAllSales();
    }

    @Operation(summary = "Get sales by document")
    @GetMapping(path = "/{documentClient}")
    private List<Sale> getTransactionsByDocument(@PathVariable int documentClient) {
        return saleService.getSalesByDocument(documentClient);
    }

    @Operation(summary = "Sell products from a specific sale")
    @PostMapping(path = "/{documentClient}/sell")
    private String makeSell(@PathVariable int documentClient, @RequestBody List<CartItem> cartItems) {
        return saleService.makeSale(documentClient, cartItems);
    }
}
