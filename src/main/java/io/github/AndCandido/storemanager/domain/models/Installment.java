package io.github.AndCandido.storemanager.domain.models;

import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "TB_INSTALLMENTS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Installment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private Double price;

    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private Boolean isPaid;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Sale sale;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
