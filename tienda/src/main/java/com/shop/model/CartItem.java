package com.shop.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Data
@Table (name = "SALE_PRODUCT")
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int itemId;
    @Column(name = "PRODUCT")
    private int productCode;
    @Column(name = "QUANTITY")
    private int quantity;
}
