package io.github.AndCandido.storemanager.domain.dtos;

import io.github.AndCandido.storemanager.domain.annotations.CanNullNotBlank;
import io.github.AndCandido.storemanager.domain.dtos.groups.UpdateValidation;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

public record CustomerDto(

        @CanNullNotBlank(message = "{customer.field.name.blank}", groups = UpdateValidation.class)
        @NotBlank(message = "{customer.field.name.blank}")
        @Length(max = 70, message = "{customer.field.name.length}")
        String name,

        String nickname,

        @CanNullNotBlank(message = "{customer.field.cpf.blank}", groups = UpdateValidation.class)
        @NotBlank(message = "{customer.field.cpf.blank}")
        @CPF(message = "{customer.field.cpf.invalid}", groups = UpdateValidation.class)
        String cpf,

        @CanNullNotBlank(message = "{customer.field.address.blank}", groups = UpdateValidation.class)
        @NotBlank(message = "{customer.field.address.blank}")
        String address,

        String phone
) {
}
