package com.shop.repository;

import com.shop.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> findAllByDocumentClient(int documentClient);
    Integer countByDocumentClientAndDateCreated(int documentClient, Date dateCreated);
}
