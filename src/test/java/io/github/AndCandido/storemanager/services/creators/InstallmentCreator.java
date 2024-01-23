package io.github.AndCandido.storemanager.services.creators;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;

import java.time.LocalDate;

public class InstallmentCreator {
    public static InstallmentRequestDto createInstallmentRequestDto(String dueDate, double price, String paymentMethod, boolean isPaid) {
        return InstallmentRequestDto.builder()
                .dueDate(LocalDate.parse(dueDate))
                .price(price)
                .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                .isPaid(isPaid)
                .build();
    };
}
