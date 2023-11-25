package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.models.Customer;

import java.util.List;
import java.util.UUID;

public interface ICustomerService {

    Customer saveCustomer(CustomerDto customerDto);

    List<Customer> getAllCustomers();

    Customer getCustomerById(UUID id);

    Customer updateCustomer(CustomerDto customerDto, UUID id);

    void deleteCustomer(UUID id);

    boolean customerExists(UUID id);
}
