package io.github.AndCandido.storemanager.domain.repositories;

import io.github.AndCandido.storemanager.domain.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ISaleRepository extends JpaRepository<Sale, UUID> {
}
