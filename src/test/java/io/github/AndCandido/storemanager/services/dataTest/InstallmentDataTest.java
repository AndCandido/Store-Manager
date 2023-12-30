package io.github.AndCandido.storemanager.services.dataTest;

import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static io.github.AndCandido.storemanager.services.creators.InstallmentCreator.*;

@Getter
@Component
public class InstallmentDataTest {

    private List<InstallmentDto> installmentsDto;

    public InstallmentDataTest createInstallments() {
        LocalDate tomorrowDate = LocalDate.now().plusDays(1);

        installmentsDto = List.of(
            createInstallmentDto(tomorrowDate.toString(), 100, "DEBIT_CARD", true)
        );

        return this;
    }
}
