package com.shop.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    public int itemId;

    private int productCode;
    private String productName;
    private int quantity;
    private double amount;
}
