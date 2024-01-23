package io.github.AndCandido.storemanager.services.dataTest;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import io.github.AndCandido.storemanager.services.creators.InstallmentCreator;

@Getter
@Component
public class InstallmentDataTest {

    private List<InstallmentRequestDto> installmentsRequestDto;

    public InstallmentDataTest createInstallments() {
        LocalDate tomorrowDate = LocalDate.now().plusDays(1);

        installmentsRequestDto = List.of(
           InstallmentCreator.createInstallmentRequestDto(tomorrowDate.toString(), 100, "DEBIT_CARD", true)
        );

        return this;
    }
}
