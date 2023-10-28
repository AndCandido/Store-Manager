package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.services.ICustomerService;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.models.CustomerModel;
import io.github.AndCandido.storemanager.domain.repositories.ICustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public CustomerModel saveCustomer(CustomerDto customerDto) {
        var customer = new CustomerModel();

        BeanUtils.copyProperties(customerDto, customer);

        return customerRepository.save(customer);
    }

    @Override
    public List<CustomerModel> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public CustomerModel updateCustomer(CustomerDto customerDto, UUID id) {
        CustomerModel customer = getCustomerById(id);

        BeanUtils.copyProperties(customerDto, customer);

        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(UUID id) {
        CustomerModel customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    @Override
    public CustomerModel getCustomerById(UUID id) {
        CustomerModel customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        return customer;
    }

    public boolean customerExists(UUID id) {
        return customerRepository.existsById(id);
    }
}