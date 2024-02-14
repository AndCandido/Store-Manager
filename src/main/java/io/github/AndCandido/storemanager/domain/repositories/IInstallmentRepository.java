package io.github.AndCandido.storemanager.domain.repositories;

import io.github.AndCandido.storemanager.domain.models.Customer;
import io.github.AndCandido.storemanager.domain.models.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IInstallmentRepository extends JpaRepository<Installment, UUID> {

    List<Installment> findByCustomerAndIsPaidFalse(Customer customer);

    List<Installment> findByCustomer(Customer customer);
}
