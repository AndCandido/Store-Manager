package io.github.AndCandido.storemanager.services.creators;

import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;

import java.time.LocalDate;

public class InstallmentCreator {
    public static InstallmentDto createInstallmentDto(String dueDate, double price, String paymentMethod, boolean isPaid) {
        return InstallmentDto.builder()
                .dueDate(LocalDate.parse(dueDate))
                .price(price)
                .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                .isPaid(isPaid)
                .build();
    };
}
