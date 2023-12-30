package io.github.AndCandido.storemanager.services.creators;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;

public class CustomerCreator {

    public static CustomerDto createCustomerDto(String name, String cpf, String nickname, String address, String phone) {
        return CustomerDto.builder()
                .name(name)
                .cpf(cpf)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .build();
    }
}
