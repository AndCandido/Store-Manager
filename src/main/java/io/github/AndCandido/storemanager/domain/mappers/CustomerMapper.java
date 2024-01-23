package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.responses.CustomerResponseDto;
import io.github.AndCandido.storemanager.domain.models.Customer;

public class CustomerMapper {

    public static CustomerResponseDto toDto(Customer customer) {
        if (customer == null) return null;

        var salesDto = SaleMapper.toDtoListWithoutAssociations(customer.getSales());
        var installmentsDto = InstallmentMapper.toDtoListWithoutAssociations(customer.getInstallments());

        return CustomerResponseDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .cpf( customer.getCpf())
                .nickname(customer.getNickname())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .sales(salesDto)
                .installments(installmentsDto)
                .createdAt(customer.getCreatedAt())
                .build();
    }

    public static CustomerResponseDto toDtoWithoutAssociations(Customer customer) {
        return customer == null ? null
                : CustomerResponseDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .cpf( customer.getCpf())
                .nickname(customer.getNickname())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
