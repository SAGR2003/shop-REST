package com.shop.controller.dto;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "products")
@Data
public class ProductDTO{
    @Id
    private int code;
    private String name;
    private double unitValue;
    private int stock;
}
