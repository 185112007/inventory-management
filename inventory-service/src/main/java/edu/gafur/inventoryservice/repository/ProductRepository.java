package edu.gafur.inventoryservice.repository;

import edu.gafur.inventoryservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT SUM(pro.price) FROM Product pro")
    BigDecimal totalProductPrice();
}
