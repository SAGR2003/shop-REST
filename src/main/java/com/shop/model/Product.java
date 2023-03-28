package com.shop.model;

import javax.persistence.*;
import java.sql.Date;

import lombok.*;

@Entity
@Table(name = "STOCK")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @Column(name = "ID")
    private int code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "VALUE")
    private int unitValue;
    @Column(name = "QUANTITY")
    private int stock;
    @Column(name = "DATE_CREATED")
    private Date dateCreated;
}