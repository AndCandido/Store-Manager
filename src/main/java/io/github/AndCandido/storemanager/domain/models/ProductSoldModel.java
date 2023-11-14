package io.github.AndCandido.storemanager.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "TB_PRODUCTS_SOLD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSoldModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private ProductModel productModel;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private SaleModel sale;

    @Column(nullable = false)
    private Integer quantity;
}

