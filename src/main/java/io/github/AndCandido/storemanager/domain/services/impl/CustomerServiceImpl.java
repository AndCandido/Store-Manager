package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.services.ICustomerService;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.models.Customer;
import io.github.AndCandido.storemanager.domain.repositories.ICustomerRepository;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;
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
    public Customer saveCustomer(CustomerDto customerDto) {
        var customer = new Customer();

        BeanUtils.copyProperties(customerDto, customer);

        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(CustomerDto customerDto, UUID id) {
        Customer customer = getCustomerById(id);

        ApplicationUtil.copyNonNullProperties(customerDto, customer);

        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(UUID id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    @Override
    public Customer getCustomerById(UUID id) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        return customer;
    }

    public boolean customerExists(UUID id) {
        return customerRepository.existsById(id);
    }
}