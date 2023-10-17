package io.github.AndCandido.storemanager.domain.repositories;

import io.github.AndCandido.storemanager.domain.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IProductRepository extends JpaRepository<ProductModel, UUID> {
}
