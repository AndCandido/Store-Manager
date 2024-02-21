package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
<<<<<<< Updated upstream
=======
import io.github.AndCandido.storemanager.domain.dtos.requests.PaymentInstallment;
>>>>>>> Stashed changes
import io.github.AndCandido.storemanager.domain.models.Customer;
import io.github.AndCandido.storemanager.domain.models.Installment;

import java.util.List;
import java.util.UUID;

public interface IInstallmentService {

    Installment saveInstallment(Installment installment);

    Installment getInstallmentById(UUID id);

    Installment updateInstallment(UUID id, InstallmentRequestDto installmentRequestDto);

    Installment setPaymentInstallment(PaymentInstallment paymentInstallment, UUID id);

    Installment createInstallment(InstallmentRequestDto installmentRequestDto);

    List<Installment> getInstallmentsByCustomerNonPaid(Customer customer);

    List<Installment> getInstallmentsByCustomer(Customer customer);
}
