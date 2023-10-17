package io.github.AndCandido.storemanager.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

public record CustomerDto(

        @NotBlank(message = "{customer.field.name.blank}")
        @Length(max = 70, message = "{customer.field.name.length}")
        String name,

        String nickname,

        @NotBlank(message = "{customer.field.cpf.blank}")
        @CPF(message = "{customer.field.cpf.invalid}")
        String cpf,

        @NotBlank(message = "{customer.field.address.blank}")
        String address,

        String phone
) {
}
