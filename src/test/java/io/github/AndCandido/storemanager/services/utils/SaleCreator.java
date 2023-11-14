package io.github.AndCandido.storemanager.services.utils;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;
import io.github.AndCandido.storemanager.domain.mappers.CustomerMapper;
import io.github.AndCandido.storemanager.domain.models.CustomerModel;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;
import io.github.AndCandido.storemanager.domain.models.SaleModel;

import java.math.BigDecimal;
import java.util.List;

public class SaleCreator {

    public static SaleModel createModel(short duplication, String paymentMethod, CustomerModel customer, double price, List<ProductSoldModel> productSoldModels) {
        return SaleModel.builder()
                .duplication(duplication)
                .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                .customer(customer)
                .price(BigDecimal.valueOf(price))
                .productsSold(productSoldModels)
                .build();
    }

    public static SaleDto createDto(short duplication, String paymentMethod, CustomerModel customer, double price, List<ProductSoldDto> productSoldDtos) {
        return SaleDto.builder()
                .duplication(duplication)
                .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                .customer(CustomerMapper.toDto(customer))
                .price(BigDecimal.valueOf(price))
                .productsSold(productSoldDtos)
                .build();
    }
}
