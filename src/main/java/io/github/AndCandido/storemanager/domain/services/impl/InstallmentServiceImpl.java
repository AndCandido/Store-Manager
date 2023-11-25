package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.mappers.InstallmentMapper;
import io.github.AndCandido.storemanager.domain.models.Installment;
import io.github.AndCandido.storemanager.domain.models.Sale;
import io.github.AndCandido.storemanager.domain.services.IInstallmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstallmentServiceImpl implements IInstallmentService {
    @Override
    public List<Installment> saveInstallmentBySale(Sale sale, SaleDto saleDto) {
        var installments = new ArrayList<Installment>(saleDto.installments().size());

        for (InstallmentDto installmentDto : saleDto.installments()) {
            var installment = InstallmentMapper.toModel(installmentDto);

            installment.setCustomer(sale.getCustomer());
            installment.setSale(sale);

            installments.add(installment);
        }

        return installments;
    }


}
