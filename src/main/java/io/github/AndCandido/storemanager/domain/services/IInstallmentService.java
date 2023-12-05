package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.Installment;
import io.github.AndCandido.storemanager.domain.models.Sale;

import java.util.List;
import java.util.UUID;

public interface IInstallmentService {

    Installment saveInstallment(Installment installment);

    Installment findById(UUID id);

    Installment patchInstallment(InstallmentDto installmentDto, UUID id);

    Installment createInstallment(InstallmentDto installmentDto);
}
