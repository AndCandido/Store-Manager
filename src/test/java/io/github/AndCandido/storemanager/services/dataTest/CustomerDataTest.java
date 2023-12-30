package io.github.AndCandido.storemanager.services.dataTest;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import static io.github.AndCandido.storemanager.services.creators.CustomerCreator.*;

import io.github.AndCandido.storemanager.domain.repositories.ICustomerRepository;
import io.github.AndCandido.storemanager.services.restTemplates.ResourceTestRestTemplate;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerDataTest {
    private static final String CUSTOMERS_URI = "/customers";
    
    @Getter
    private List<CustomerDto> customersDto;
    
    private final ResourceTestRestTemplate<CustomerDto, UUID> restTemplate;
    private final ICustomerRepository customerRepository;
    
    public CustomerDataTest(ResourceTestRestTemplate<CustomerDto, UUID> restTemplate, ICustomerRepository customerRepository) {
        this.restTemplate = restTemplate;
        this.customerRepository = customerRepository;
    }

    public CustomerDataTest createCustomers() {
        customersDto = List.of(
            createCustomerDto("Alberto Lima Castro", "021.419.780-84", null, "Rua. ", null)
        );

        return this;
    }

    public CustomerDataTest saveCustomers() {
        customersDto = restTemplate
            .postAll(CUSTOMERS_URI, customersDto, new ParameterizedTypeReference<CustomerDto>() {}).stream()
            .map(response -> {
                Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
                return response.getBody();
            })
            .toList();;

        return this;
    }

    public CustomerDto getCustomerDto(int i) {
        return customersDto.get(i);
    }

    public void deleteCustomer(CustomerDto customerDto) {
        restTemplate.delete(CUSTOMERS_URI, customerDto.id());
    }

    public ResponseEntity<CustomerDto> findCustomer(CustomerDto customerDto) {
        return restTemplate.getById(CUSTOMERS_URI, customerDto.id(), new ParameterizedTypeReference<>() {});
    }

    public void cleanDataBase() {
        customerRepository.deleteAll();
    }
}
