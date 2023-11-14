package io.github.AndCandido.storemanager.services.utils;

import io.github.AndCandido.storemanager.domain.models.CustomerModel;

public class CustomerCreator {
    public static CustomerModel createModel(String name, String cpf, String nickname, String address, String phone) {
        return new CustomerModel(
                null, name, cpf, nickname, address, phone, null, null
        );
    }
}
