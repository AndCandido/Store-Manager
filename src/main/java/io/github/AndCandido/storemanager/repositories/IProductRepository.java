package io.github.AndCandido.storemanager.repositories;

import io.github.AndCandido.storemanager.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IProductRepository extends JpaRepository<Product, UUID> {
}
