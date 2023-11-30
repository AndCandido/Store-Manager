package io.github.AndCandido.storemanager.domain.repositories;

import io.github.AndCandido.storemanager.domain.models.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IInstallmentRepository extends JpaRepository<Installment, UUID> {
}
