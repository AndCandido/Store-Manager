package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import io.github.AndCandido.storemanager.domain.models.Installment;
import io.github.AndCandido.storemanager.domain.dtos.responses.InstallmentResponseDto;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class InstallmentMapper {

    public static InstallmentResponseDto toDto(Installment installment) {
        if(installment == null) return null;

        var customerWithoutAssociations = CustomerMapper.toDtoWithoutAssociations(installment.getCustomer());
        var saleWithoutAssociations = SaleMapper.toDtoWithoutAssociations(installment.getSale());

        return InstallmentResponseDto.builder()
                .id(installment.getId())
                .dueDate(installment.getDueDate())
                .price(installment.getPrice())
                .paymentMethod(installment.getPaymentMethod())
                .isPaid(installment.getIsPaid())
                .customer(customerWithoutAssociations)
                .sale(saleWithoutAssociations)
                .createdAt(installment.getCreatedAt())
                .build();
    }

    public static Installment toModel(InstallmentRequestDto installmentRequestDto) {
        var installment = new Installment();
        BeanUtils.copyProperties(installmentRequestDto, installment);
        return installment;
    }

    public static InstallmentResponseDto toDtoWithoutAssociations(Installment installment) {
        if(installment == null) return null;

        return InstallmentResponseDto.builder()
                .id(installment.getId())
                .dueDate(installment.getDueDate())
                .price(installment.getPrice())
                .paymentMethod(installment.getPaymentMethod())
                .isPaid(installment.getIsPaid())
                .createdAt(installment.getCreatedAt())
                .build();
    }

    public static List<InstallmentResponseDto> toDtoListWithoutAssociations(List<Installment> installments) {
        if(installments == null || installments.isEmpty()) return null;

        return installments.stream().map(InstallmentMapper::toDtoWithoutAssociations).toList();
    }
}
