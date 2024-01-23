package io.github.AndCandido.storemanager.domain.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Builder
public record CustomerRequestDto(

    @NotBlank(message = "{customer.field.name.blank}")
    @Length(max = 70, message = "{customer.field.name.length}")
    String name,

    String nickname,

    @NotBlank(message = "{customer.field.cpf.blank}")
    @CPF(message = "{customer.field.cpf.invalid}")
    String cpf,

    @NotBlank(message = "{customer.field.address.blank}")
    String address,

    String phone,

    @Valid
    List<SaleRequestDto> sales,

    @Valid
    List<InstallmentRequestDto> installments
) {
}
