package com.shop.service;

import com.shop.model.CartItem;
import com.shop.model.Product;
import com.shop.model.Sale;
import com.shop.model.SaleMemento;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.SaleRepository;
import com.shop.service.exception.ClientNotFoundException;
import com.shop.service.exception.DailyTransactionLimitExceededException;
import com.shop.service.exception.InsufficientStockException;
import com.shop.service.exception.ProductNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;

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
    void When_getAllSales_Then_return_SalesList() {
        List<Sale> testSales = List.of(new Sale(1, 1019283, 1291, new Date(System.currentTimeMillis()), "Cll123", List.of(new CartItem(1, 2, 3, 1))));
        Mockito.when(saleRepository.findAll()).thenReturn(testSales);

        List<Sale> actualSales = saleService.getAllSales();

        Assertions.assertEquals(testSales, actualSales);
        Mockito.verify(saleRepository).findAll();
    }

    @Test
    void Given_document_exceeding_daily_limit_When_makeSale_Then_throw_DailyTransactionLimitExceededException() {
        int documentClient = 123456;
        List<CartItem> cartItems = Arrays.asList(new CartItem(1, 1, 2, null), new CartItem(2, 2, 1, null));
        Mockito.when(saleRepository.countByDocumentClientAndDateCreated(Mockito.eq(documentClient), Mockito.any(Date.class))).thenReturn(3);

        Assertions.assertThrows(DailyTransactionLimitExceededException.class, () -> saleService.makeSale(documentClient, cartItems, "Cll123"));
        Mockito.verify(saleRepository).countByDocumentClientAndDateCreated(Mockito.eq(documentClient), Mockito.any(Date.class));
    }

    @Test
    void Given_cartItem_with_unexisting_productCode_stock_When_makeSale_Then_throw_ProductNotFoundException() {
        int documentClient = 123456;
        List<CartItem> cartItems = List.of(new CartItem(1, 1, 2, null));
        Mockito.when(productRepository.existsById(1)).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class, () -> saleService.makeSale(documentClient, cartItems, "Cll123"));
        Mockito.verify(productRepository).existsById(1);
    }

    @Test
    void Given_quantity_of_cartItem_exceeding_product_stock_When_makeSale_Then_throw_InsufficientStockException() {
        int documentClient = 123;
        List<CartItem> cartItems = List.of(new CartItem(1, 1, 100, null));
        Product product = new Product(1, "Chocorramo", 2000, 10, new Date(System.currentTimeMillis()));

        Mockito.when(productRepository.existsById(1)).thenReturn(true);
        Mockito.when(productRepository.getById(1)).thenReturn(product);

        Assertions.assertThrows(InsufficientStockException.class, () -> saleService.makeSale(documentClient, cartItems, "Cll123"));
        Mockito.verify(productRepository).existsById(1);
        Mockito.verify(productRepository).getById(1);
    }

    @Test
    void Given_valid_document_When_getSalesByDocument_Then_return_List_of_sales_by_document() {
        int validDocument = 555;
        List<Sale> expectedSales = Arrays.asList(new Sale(1, validDocument, 1000, new Date(System.currentTimeMillis()), "Cll123", List.of(new CartItem())), new Sale(2, validDocument, 10000, new Date(System.currentTimeMillis()), "Cll123", List.of(new CartItem())));
        Mockito.when(saleRepository.findAllByDocumentClient(validDocument)).thenReturn(expectedSales);

        List<Sale> actualSales = saleService.getSalesByDocument(validDocument);

        Assertions.assertEquals(expectedSales, actualSales);
        Mockito.verify(saleRepository, Mockito.times(2)).findAllByDocumentClient(validDocument);
    }

    @Test
    void Given_invalid_document_When_getSalesByDocument_Then_throw_ClientNotFoundException() {
        int invalidDocument = 321;
        Mockito.when(saleRepository.findAllByDocumentClient(invalidDocument)).thenReturn(new ArrayList<>());

        Assertions.assertThrows(ClientNotFoundException.class, () -> saleService.getSalesByDocument(invalidDocument));
        Mockito.verify(saleRepository).findAllByDocumentClient(invalidDocument);
    }

    @Test
    void Given_salesHistory_When_undoSale_Then_return_successful_message() {
        Stack<SaleMemento> salesHistory = new Stack<>();
        saleService.setSalesHistory(salesHistory);
        SaleMemento previousSale = Mockito.mock(SaleMemento.class);
        salesHistory.push(previousSale);

        Sale sale = Mockito.mock(Sale.class);
        Mockito.when(previousSale.getSale()).thenReturn(sale);
        Mockito.when(sale.getId()).thenReturn(1);

        String result = saleService.undoSale();

        Assertions.assertEquals("The sale was undone successfully", result);
        Mockito.verify(saleRepository, Mockito.times(1)).deleteById(1);
        Assertions.assertEquals(0, salesHistory.size());
    }

    @Test
    void Given_noPreviousSale_When_undoSale_thenReturn_noSalesToUndoMessage() {
        Stack<SaleMemento> salesHistory = new Stack<>();
        saleService.setSalesHistory(salesHistory);

        String result = saleService.undoSale();

        Assertions.assertEquals("There are no sales to undo", result);
        Mockito.verify(saleRepository, Mockito.times(0)).deleteById(anyInt());
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

}