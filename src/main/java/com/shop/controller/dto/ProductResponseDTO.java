package com.shop.controller.dto;

import com.shop.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private int code;
    private String name;
    private int unitValue;
    private int stock;
    private Date dateCreated;
}
