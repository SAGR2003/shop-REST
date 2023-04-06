package com.shop.controller;

import com.shop.AbstractTest;
import com.shop.controller.dto.ListResponseDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void testGetAllTransactions() {
        List<CartItem> cartItems = createAndSaveCartItems();

        System.out.println(cartItems);
        Sale sale = new Sale(1, 123, 555, new Date(System.currentTimeMillis()), cartItems);
        System.out.println(sale);
        saleRepository.save(sale);

        ResponseEntity<ListResponseDTO> response = restTemplate.getForEntity("/sales", ListResponseDTO.class);

        assertEquals(1, response.getBody().getListResponse().size());
        assertEquals("{id=1, documentClient=123, totalAmount=555, dateCreated=" + sale.getDateCreated() + ", cartItems=[{itemId=1, productCode=1, quantity=2, saleId=1}, {itemId=2, productCode=2, quantity=3, saleId=1}]}", String.valueOf(response.getBody().getListResponse().get(0)));
    }

    @Test
    void Given_valid_document_When_getTransactionsByDocument_Then_return_ListOfSalesByDocument() {
        int document = 123;
        List<CartItem> cartItems = createAndSaveCartItems();
        Sale sale1 = new Sale(1, document, 1111111, new Date(System.currentTimeMillis()), cartItems);
        saleRepository.save(sale1);

        ResponseEntity<ListResponseDTO> response = restTemplate.getForEntity(PATH_SALE + "/{documentClient}", ListResponseDTO.class, document);

        List<Sale> sales = response.getBody().getListResponse();
        Assertions.assertNotNull(sales);
        assertEquals("{id=1, documentClient=123, totalAmount=1111111, dateCreated=" + sale1.getDateCreated() + ", cartItems=[{itemId=1, productCode=1, quantity=2, saleId=1}, {itemId=2, productCode=2, quantity=3, saleId=1}]}", String.valueOf(response.getBody().getListResponse().get(0)));
    }

    @Test
    void Given_documentClient_and_cartItems_When_makeSale_Then_return_ResponseDTO() {
        Product testProduct = new Product(1, "Gelatina", 1299, 100, new Date(System.currentTimeMillis()));
        CartItem testItem1 = new CartItem(1, testProduct.getCode(), 3, 80);
        Sale sale = new Sale(80, 1019283, 1291, new Date(System.currentTimeMillis()), Arrays.asList(testItem1));
        List<CartItem> cartItems = Arrays.asList(testItem1);
        productRepository.save(testProduct);
        cartItemRepository.save(testItem1);
        saleRepository.save(sale);
        ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(PATH_SALE + "/1019283/sell", cartItems, ResponseDTO.class);
        System.out.println(response.getBody().getResponse());
        assertEquals("I sell 3x Gelatina / Total = 3897", response.getBody().getResponse());
    }

    private List<CartItem> createAndSaveCartItems() {
        List<CartItem> cartItems = Arrays.asList(new CartItem(1, 1, 2, 1), new CartItem(2, 2, 3, 1));
        for (CartItem item : cartItems) {
            cartItemRepository.save(item);
        }
        return cartItems;
    }
}
