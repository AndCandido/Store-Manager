package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.Installment;
import io.github.AndCandido.storemanager.domain.models.Sale;

import java.util.List;

public interface IInstallmentService {


    List<Installment> saveInstallmentBySale(Sale sale, SaleDto saleDto);

}
