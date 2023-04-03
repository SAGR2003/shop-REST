package com.shop.service;

import com.shop.model.CartItem;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.SaleRepository;
import com.shop.service.exception.DailyTransactionLimitExceededException;
import com.shop.service.exception.ProductNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {
    @InjectMocks
    SaleService saleService;
    @Mock
    SaleRepository saleRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @Test
    void Given_document_exceeding_daily_limit_When_makeSale_Then_throw_DailyTransactionLimitExceededException() {
        int documentClient = 123456;
        List<CartItem> cartItems = Arrays.asList(new CartItem(1, 1, 2, null), new CartItem(2, 2, 1, null));
        Mockito.when(saleRepository.countByDocumentClientAndDateCreated(Mockito.eq(documentClient), Mockito.any(Date.class))).thenReturn(3);

        Assertions.assertThrows(DailyTransactionLimitExceededException.class, () -> saleService.makeSale(documentClient, cartItems));
        Mockito.verify(saleRepository).countByDocumentClientAndDateCreated(Mockito.eq(documentClient), Mockito.any(Date.class));
    }

    @Test
    void Given_cartItem_with_unexisting_productCode_stock_When_makeSale_Then_throw_ProductNotFoundException() {
        int documentClient = 123456;
        List<CartItem> cartItems = List.of(new CartItem(1, 1, 2, null));
        Mockito.when(productRepository.existsById(1)).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class, () -> saleService.makeSale(documentClient, cartItems));
        Mockito.verify(productRepository).existsById(1);
    }
}