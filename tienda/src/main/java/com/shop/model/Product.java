package com.shop.model;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private int code;
    private String name;
    private double unitValue;
    private int stock;
}