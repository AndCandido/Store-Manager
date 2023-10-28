package io.github.AndCandido.storemanager.domain.models;

import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "TB_SALES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private short duplication;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne
    private CustomerModel customer;

    @OneToMany(mappedBy = "sale")
    private List<ProductSoldModel> productsSold = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
