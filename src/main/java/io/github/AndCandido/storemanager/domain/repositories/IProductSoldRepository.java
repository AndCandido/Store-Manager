package io.github.AndCandido.storemanager.domain.repositories;

import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IProductSoldRepository extends JpaRepository<ProductSoldModel, UUID> {
    List<ProductSoldModel> findByProductModel(ProductModel productModel4);
}
