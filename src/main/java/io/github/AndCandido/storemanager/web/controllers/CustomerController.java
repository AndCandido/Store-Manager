package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.mappers.CustomerMapper;
import io.github.AndCandido.storemanager.domain.models.CustomerModel;
import io.github.AndCandido.storemanager.domain.services.ICustomerService;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody @Valid CustomerDto customerDto) {
        CustomerModel customer = customerService.saveCustomer(customerDto);
        var customerResDto = CustomerMapper.toDto(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResDto);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerModel> customers = customerService.getAllCustomers();

        if(customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<CustomerDto>());
        }

        List<CustomerDto> customerResDtos = customers.stream()
                .map(CustomerMapper::toDto).toList();

        return ResponseEntity.status(HttpStatus.OK).body(customerResDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(
            @PathVariable UUID id
    ) {
       CustomerModel customer = customerService.getCustomerById(id);
       var customerResDto = CustomerMapper.toDto(customer);
       return ResponseEntity.ok(customerResDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @RequestBody @Valid CustomerDto customerDto,
            @PathVariable UUID id
    ) {
        CustomerModel customer = customerService.updateCustomer(customerDto, id);
        var customerResDto = CustomerMapper.toDto(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
    }
}
