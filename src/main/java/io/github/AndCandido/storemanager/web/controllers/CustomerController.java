package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.requests.CustomerRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.CustomerResponseDto;
import io.github.AndCandido.storemanager.domain.mappers.CustomerMapper;
import io.github.AndCandido.storemanager.domain.models.Customer;
import io.github.AndCandido.storemanager.domain.services.ICustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDto> saveCustomer(
        @RequestBody @Valid CustomerRequestDto customerRequestDto
    ) {
        Customer customer = customerService.saveCustomer(customerRequestDto);
        var customerResDto = CustomerMapper.toDto(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResDto);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();

        if(customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<CustomerResponseDto>());
        }

        List<CustomerResponseDto> customersResDto = customers.stream()
                .map(CustomerMapper::toDto).toList();

        return ResponseEntity.status(HttpStatus.OK).body(customersResDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(
            @PathVariable UUID id
    ) {
       Customer customer = customerService.getCustomerById(id);
       var customerResDto = CustomerMapper.toDto(customer);
       return ResponseEntity.ok(customerResDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @RequestBody @Valid CustomerRequestDto customerRequestDto,
            @PathVariable UUID id
    ) {
        Customer customer = customerService.updateCustomer(customerRequestDto, id);
        var customerResDto = CustomerMapper.toDto(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
    }
}
