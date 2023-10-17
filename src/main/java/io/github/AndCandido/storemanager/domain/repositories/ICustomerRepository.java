package io.github.AndCandido.storemanager.domain.repositories;

import io.github.AndCandido.storemanager.domain.models.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICustomerRepository extends JpaRepository<CustomerModel, UUID> {
}
