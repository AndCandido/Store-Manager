package io.github.AndCandido.storemanager.domain.repositories;

import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IProductSoldRepository extends JpaRepository<ProductSoldModel, UUID> {
}
