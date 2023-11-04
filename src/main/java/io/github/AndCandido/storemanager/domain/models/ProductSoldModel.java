package io.github.AndCandido.storemanager.domain.models;

import com.fasterxml.jackson.annotation.*;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

