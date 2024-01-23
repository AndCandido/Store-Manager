package io.github.AndCandido.storemanager.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "TB_CUSTOMERS")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 70)
    private String name;

    @Column(nullable = false, length = 14)
    private String cpf;

    private String nickname;

    @Column(nullable = false)
    private String address;

    private String phone;

    @OneToMany(mappedBy = "customer")
    private List<Sale> sales;

    @OneToMany(mappedBy = "customer")
    private List<Installment> installments;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @PreRemove
    public void setCustomerNullOn() {
        if(!isNullOrEmpty(sales)) {
            sales.forEach(sale -> sale.setCustomer(null));
        }
        if(!isNullOrEmpty(installments)) {
            installments.forEach(installment -> installment.setCustomer(null));
        }
    }

    private boolean isNullOrEmpty(List<?> items) {
        return items == null || items.isEmpty();
    }
}
