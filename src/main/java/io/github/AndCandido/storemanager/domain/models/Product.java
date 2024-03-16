package io.github.AndCandido.storemanager.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "TB_PRODUCTS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long ref;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "product")
    private List<ProductSold> productsSold;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @PreRemove
    public void preRemove() {
        if(productsSold == null || productsSold.isEmpty())
            return;

        for (ProductSold productSold : productsSold) {
            productSold.setProduct(null);
        }
    }
}
