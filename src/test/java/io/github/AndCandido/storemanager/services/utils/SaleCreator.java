package io.github.AndCandido.storemanager.services.utils;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;

import java.util.List;

public class SaleCreator {

    public static SaleDto createDto(CustomerDto customer, double price, List<ProductSoldDto> productsSold, List<InstallmentDto> installmentsDto) {
        return SaleDto.builder()
                .customer(customer)
                .price(price)
                .productsSold(productsSold)
                .installments(installmentsDto)
                .build();
    }
}
