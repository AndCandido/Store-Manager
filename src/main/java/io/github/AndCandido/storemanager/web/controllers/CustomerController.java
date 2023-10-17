package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.dtos.groups.UpdateValidation;
import io.github.AndCandido.storemanager.domain.models.CustomerModel;
import io.github.AndCandido.storemanager.domain.services.ICustomerService;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerModel> saveCustomer(@RequestBody @Valid CustomerDto customerDto) {
        CustomerModel customer = customerService.saveCustomer(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerModel>> getAllCustomers() {
        List<CustomerModel> customers = customerService.getAllCustomers();

        boolean isCustomersEmpty = customers.size() < 1;

        var httpStatus = isCustomersEmpty ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerModel> getCustomerById(
            @PathVariable UUID id
    ) {
       CustomerModel customer = customerService.getCustomerById(id);
       return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerModel> updateCustomer(
            @RequestBody @Validated(UpdateValidation.class) CustomerDto customerDto,
            @PathVariable UUID id
    ) {
        CustomerModel customer = customerService.updateCustomer(customerDto, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
    }
}
