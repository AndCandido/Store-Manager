package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.models.CustomerModel;

import java.util.List;
import java.util.UUID;

public interface ICustomerService {

    CustomerModel saveCustomer(CustomerDto customerDto);

    List<CustomerModel> getAllCustomers();

    CustomerModel getCustomerById(UUID id);

    CustomerModel updateCustomer(CustomerDto customerDto, UUID id);

    void deleteCustomer(UUID id);
}
