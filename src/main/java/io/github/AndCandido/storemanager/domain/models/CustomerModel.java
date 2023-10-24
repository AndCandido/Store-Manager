package io.github.AndCandido.storemanager.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "TB_CUSTOMERS")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 70)
    private String name;

    @Column(nullable = false, length = 14)
    private String cpf;

    private String nickname;

    @Column(nullable = false)
    private String address;

    private String phone;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
