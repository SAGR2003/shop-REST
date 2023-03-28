package com.shop.controller;

import com.shop.controller.dto.ListResponseDTO;
import com.shop.controller.dto.ResponseDTO;
import com.shop.model.CartItem;
import com.shop.service.ISale;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@AllArgsConstructor
public class SalesController {
    private ISale saleService;

    @Operation(summary = "Get all Sales")
    @GetMapping(path = "")
    private ListResponseDTO getAllTransactions() {
        return new ListResponseDTO(saleService.getAllSales());
    }

    @Operation(summary = "Get sales by document")
    @GetMapping(path = "/{documentClient}")
    private ListResponseDTO getTransactionsByDocument(@PathVariable int documentClient) {
        return new ListResponseDTO(saleService.getSalesByDocument(documentClient));
    }

    @Operation(summary = "Sell products from a specific sale")
    @PostMapping(path = "/{documentClient}/sell")
    private ResponseDTO makeSell(@PathVariable int documentClient, @RequestBody List<CartItem> cartItems) {
        return new ResponseDTO(saleService.makeSale(documentClient, cartItems));
    }
}
