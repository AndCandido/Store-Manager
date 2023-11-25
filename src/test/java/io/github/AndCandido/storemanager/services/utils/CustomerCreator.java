package io.github.AndCandido.storemanager.services.utils;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.models.Customer;

public class CustomerCreator {

    public static Customer createModel(String name, String cpf, String nickname, String address, String phone) {
        return Customer.builder()
                .name(name)
                .cpf(cpf)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .build();
    }
    public static CustomerDto createDto(String name, String cpf, String nickname, String address, String phone) {
        return CustomerDto.builder()
                .name(name)
                .cpf(cpf)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .build();
    }
}
