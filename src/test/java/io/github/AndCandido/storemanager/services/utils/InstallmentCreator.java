package io.github.AndCandido.storemanager.services.utils;

import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InstallmentCreator {
    public static InstallmentDto createDto(String dueDate, double price, String paymentMethod, boolean isPaid) {
        return InstallmentDto.builder()
                .dueDate(LocalDate.parse(dueDate))
                .price(BigDecimal.valueOf(price))
                .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                .isPaid(isPaid)
                .build();
    };
}
