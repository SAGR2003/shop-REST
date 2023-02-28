package com.shop.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    private int productCode;
    private String productName;
    private int quantity;
    private double amount;
}
