package com.shop.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

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
    @Column(name = "UNIT_VALUE")
    private int unitValue;
    @Column(name = "QUANTITY")
    private int stock;
    @Column(name = "DATE_CREATED")
    private Date dateCreated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return code == product.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}