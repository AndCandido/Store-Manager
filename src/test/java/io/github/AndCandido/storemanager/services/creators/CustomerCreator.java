package io.github.AndCandido.storemanager.services.creators;

import io.github.AndCandido.storemanager.domain.dtos.requests.CustomerRequestDto;

public class CustomerCreator {

    public static CustomerRequestDto createCustomerRequestDto(String name, String cpf, String nickname, String address, String phone) {
        return CustomerRequestDto.builder()
                .name(name)
                .cpf(cpf)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .build();
    }
}
