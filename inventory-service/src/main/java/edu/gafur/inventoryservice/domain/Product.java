package edu.gafur.inventoryservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "p_name")
    private String name;

    @Column(name = "p_description")
    private String description;

    @Column(name = "p_price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "p_category_id")
    private ProductCategory category;
}
