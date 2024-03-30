package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.requests.CustomerRequestDto;
import io.github.AndCandido.storemanager.domain.models.Customer;
import io.github.AndCandido.storemanager.domain.models.Installment;

import java.util.List;
import java.util.UUID;

public interface ICustomerService {

    Customer saveCustomer(CustomerRequestDto customerRequestDto);

    List<Customer> getAllCustomers();

    Customer getCustomerById(UUID id);

    Customer updateCustomer(CustomerRequestDto customerRequestDto, UUID id);

    void deleteCustomer(UUID id);

    boolean customerExists(UUID id);

    List<Installment> getCustomerInstallments(UUID customerId, boolean isInstallmentsNonPaid);

    List<Customer> getCustomersByName(String customerName);
}
