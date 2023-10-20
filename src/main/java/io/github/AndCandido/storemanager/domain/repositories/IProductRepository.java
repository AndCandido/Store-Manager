package io.github.AndCandido.storemanager.domain.repositories;

import io.github.AndCandido.storemanager.domain.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<ProductModel, Long> {
}
