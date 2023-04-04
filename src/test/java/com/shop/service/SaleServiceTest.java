package com.shop.service;

import com.shop.model.CartItem;
import com.shop.model.Product;
import com.shop.model.Sale;
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
    void When_getAllSales_Then_return_SalesList(){
        List<Sale> testSales = Arrays.asList(new Sale(1,1019283,1291,new Date(System.currentTimeMillis()),Arrays.asList(new CartItem(1,2,3,1))));
        Mockito.when(saleRepository.findAll()).thenReturn(testSales);

        List<Sale> actualSales =saleService.getAllSales();

        Assertions.assertEquals(testSales,actualSales);
        Mockito.verify(saleRepository).findAll();
    }
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
    @Test
    void Given_valid_documentClient_and_cartItems_When_makeSale_Then_createBill(){
        Product testProduct  = new Product(1,"Gelatina",1299,100,new Date(System.currentTimeMillis()));
        CartItem testItem1=new CartItem(1, testProduct.getCode(),3,null);
        Sale sale=new Sale(1,1019283,1291,new Date(System.currentTimeMillis()), Arrays.asList(testItem1));

        Mockito.when(productRepository.existsById(testProduct.getCode())).thenReturn(true);
        Mockito.when(productRepository.getById(testProduct.getCode())).thenReturn(testProduct);

        String actualBill = saleService.makeSale(sale.getDocumentClient(),sale.getCartItems());
        Assertions.assertEquals(actualBill,"I sell 3x Gelatina / Total = 3897");


        Mockito.verify(productRepository).save(testProduct);
        Mockito.verify(saleRepository).save(Mockito.any(Sale.class));
        Mockito.verify(cartItemRepository).save(testItem1);
    }
}