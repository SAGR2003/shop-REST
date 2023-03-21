package com.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Component
@Entity
@Table(name = "SALE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "DOCUMENT_CLIENT")
    private int documentClient;
    @Column(name = "TOTAL_AMOUNT")
    private int totalAmount;
    @Column(name = "DATE_CREATED")
    private Date dateCreated;
}

