package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import io.github.AndCandido.storemanager.domain.models.Installment;

import java.util.UUID;

public interface IInstallmentService {

    Installment saveInstallment(Installment installment);

    Installment findById(UUID id);

    Installment patchInstallment(InstallmentRequestDto installmentRequestDto, UUID id);

    Installment createInstallment(InstallmentRequestDto installmentRequestDto);
}
