package io.github.AndCandido.storemanager.services.dataTest;

import io.github.AndCandido.storemanager.domain.dtos.requests.CustomerRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.CustomerResponseDto;
import io.github.AndCandido.storemanager.services.creators.CustomerCreator;

import io.github.AndCandido.storemanager.domain.repositories.ICustomerRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerDataTest {
    public static final String CUSTOMERS_URI = "/customers";

    @Getter
    private List<CustomerRequestDto> customersRequestDto;

    @Getter@Setter
    private List<CustomerResponseDto> customersSaved;

    private final ICustomerRepository customerRepository;
    
    public CustomerDataTest(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDataTest createRequestCustomers() {
        customersRequestDto = List.of(
            CustomerCreator.createCustomerRequestDto("Alberto Lima Castro", "021.419.780-84", null, "Rua. ", null)
        );

        return this;
    }

    public CustomerResponseDto getCustomerResponseDto(int i) {
        return customersSaved.get(i);
    }

    public void cleanDataBase() {
        customerRepository.deleteAll();
    }
}
