package com.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncSale {
    private int documentClient;
    private List<CartItem> cartItems;
    private String address;
}

