package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.models.CustomerModel;

public class CustomerMapper {
    public static CustomerModel toModel(CustomerDto customerDto) {
        return customerDto == null ? null :
                CustomerModel.builder()
                .id(customerDto.id())
                .name(customerDto.name())
                .cpf(customerDto.cpf())
                .nickname(customerDto.nickname())
                .address(customerDto.address())
                .phone(customerDto.phone())
                .saleModels(SaleMapper.toModelList(customerDto.sales()))
                .build();
    }

    public static CustomerDto toDto(CustomerModel customerModel) {
        var saleModels = customerModel.getSaleModels();
        var saleDtos = saleModels == null || saleModels.isEmpty() ? null
                : SaleMapper.toResponseDtoList(saleModels);

        return new CustomerDto(
                customerModel.getId(),
                customerModel.getName(),
                customerModel.getCpf(),
                customerModel.getNickname(),
                customerModel.getAddress(),
                customerModel.getPhone(),
                saleDtos,
                customerModel.getCreatedAt()
        );
    }

    public static CustomerDto toResponseDto(CustomerModel customerModel) {
        return new CustomerDto(
                customerModel.getId(),
                customerModel.getName(),
                customerModel.getCpf(),
                customerModel.getNickname(),
                customerModel.getAddress(),
                customerModel.getPhone(),
                null,
                customerModel.getCreatedAt()
        );
    }
}
