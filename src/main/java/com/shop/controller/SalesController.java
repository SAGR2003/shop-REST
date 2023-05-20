package com.shop.controller;

import com.google.gson.Gson;
import com.shop.controller.dto.ListResponseDTO;
import com.shop.controller.dto.ResponseDTO;
import com.shop.model.AsyncSale;
import com.shop.model.CartItem;
import com.shop.service.ISale;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@AllArgsConstructor
@Tag(name = "sales-controller", description = "Manage Sales")
public class SalesController {
    private ISale saleService;
    private Gson gson;

    @Operation(summary = "Get all Sales")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Successfull"), @ApiResponse(code = 500, message = "Unexpected error.")})
    @GetMapping(path = "")
    private ListResponseDTO getAllTransactions() {
        return new ListResponseDTO(saleService.getAllSales());
    }

    @Operation(summary = "Get sales by document")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Successfull"), @ApiResponse(code = 500, message = "Unexpected error. The document client does not exist"), @ApiResponse(code = 400, message = "Bad request. The document client must be integer and greater than 0")})
    @GetMapping(path = "/{documentClient}")
    private ListResponseDTO getTransactionsByDocument(@PathVariable int documentClient) {
        return new ListResponseDTO(saleService.getSalesByDocument(documentClient));
    }

    @Operation(summary = "Sell products from a specific sale")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Successfull"), @ApiResponse(code = 500, message = "Unexpected error. There is not stock enough and the code does not exists"), @ApiResponse(code = 400, message = "Bad request. Invalid request syntax")})
    @PostMapping(path = "/{documentClient}/sell")
    private ResponseDTO makeSell(@PathVariable int documentClient, @RequestBody List<CartItem> cartItems) {
        return new ResponseDTO(saleService.makeSale(documentClient, cartItems));
    }

    @Operation(summary = "Sell products in an asynchronous way")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Request Successfull"), @ApiResponse(code = 500, message = "Unexpected error. There is not stock enough and the code does not exists"), @ApiResponse(code = 400, message = "Bad request. Invalid request syntax")})
    @RabbitListener(queues = "asynchronousSaleQueue")
    private void makeSellAsynchronous(String shoppingCart) {
        AsyncSale asyncSale = gson.fromJson(shoppingCart, AsyncSale.class);
        saleService.makeSaleAsync(asyncSale);
    }

}
