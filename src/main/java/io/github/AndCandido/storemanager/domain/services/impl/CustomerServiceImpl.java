package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.domain.dtos.requests.CustomerRequestDto;
import io.github.AndCandido.storemanager.domain.services.ICustomerService;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.models.Customer;
import io.github.AndCandido.storemanager.domain.repositories.ICustomerRepository;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final ICustomerRepository customerRepository;

    @Override
    public Customer saveCustomer(CustomerRequestDto customerRequestDto) {
        var customer = new Customer();
        BeanUtils.copyProperties(customerRequestDto, customer);

        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(CustomerRequestDto customerRequestDto, UUID id) {
        Customer customer = getCustomerById(id);
        ApplicationUtil.copyNonNullProperties(customerRequestDto, customer);

        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(UUID id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    @Override
    public Customer getCustomerById(UUID id) {
        return customerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public boolean customerExists(UUID id) {
        return customerRepository.existsById(id);
    }
}