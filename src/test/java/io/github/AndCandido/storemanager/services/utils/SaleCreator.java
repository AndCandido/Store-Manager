package io.github.AndCandido.storemanager.services.utils;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.mappers.CustomerMapper;
import io.github.AndCandido.storemanager.domain.models.Customer;
import io.github.AndCandido.storemanager.domain.models.ProductSold;
import io.github.AndCandido.storemanager.domain.models.Sale;

import java.math.BigDecimal;
import java.util.List;

public class SaleCreator {

    public static Sale createModel(Customer customer, double price, List<ProductSold> productsSold) {
        return Sale.builder()
                .customer(customer)
                .price(BigDecimal.valueOf(price))
                .productsSold(productsSold)
                .build();
    }

    public static SaleDto createDto(CustomerDto customer, double price, List<ProductSoldDto> productsSold, List<InstallmentDto> installmentsDto) {
        return SaleDto.builder()
                .customer(customer)
                .price(BigDecimal.valueOf(price))
                .productsSold(productsSold)
                .installments(installmentsDto)
                .build();
    }
}
