package com.shop.controller.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @Id
    private int code;
    private String name;
    private double unitValue;
    private int stock;
}