package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CustomerMapper {

    public static CustomerDto toDto(Customer customer) {
        if (customer == null) return null;

        var salesDto = SaleMapper.toCustomerResponseDtoList(customer.getSales());
        var installmentsDto = InstallmentMapper.toResponseDtoList(customer.getInstallments());

        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getCpf(),
                customer.getNickname(),
                customer.getAddress(),
                customer.getPhone(),
                salesDto,
                installmentsDto,
                customer.getCreatedAt()
        );
    }

    public static CustomerDto toSaleResponseDto(Customer customer) {
        return customer == null ? null
                : new CustomerDto(
                    customer.getId(),
                    customer.getName(),
                    customer.getCpf(),
                    customer.getNickname(),
                    customer.getAddress(),
                    customer.getPhone(),
                    null,
                    null,
                    customer.getCreatedAt()
        );
    }
}
