package com.shop.controller;

import com.shop.AbstractTest;
import com.shop.controller.dto.ResponseDTO;
import com.shop.model.CartItem;
import com.shop.model.Product;
import com.shop.model.Sale;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.SaleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;


class SalesControllerTest extends AbstractTest {
    private static final String PATH_SALE = "/sales";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Test
    void Given_documentClient_and_cartItems_When_makeSale_Then_return_ResponseDTO(){
        Product testProduct  = new Product(1,"Gelatina",1299,100,new Date(System.currentTimeMillis()));
        CartItem testItem1=new CartItem(1, testProduct.getCode(),3,80);
        Sale sale=new Sale(80,1019283,1291,new Date(System.currentTimeMillis()), Arrays.asList(testItem1));
        List<CartItem> cartItems = Arrays.asList(testItem1);
        productRepository.save(testProduct);
        cartItemRepository.save(testItem1);
        saleRepository.save(sale);
        ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(PATH_SALE + "/1019283/sell",cartItems,ResponseDTO.class);
        System.out.println(response.getBody().getResponse());
        Assertions.assertEquals( "I sell 3x Gelatina / Total = 3897",response.getBody().getResponse());
    }

}