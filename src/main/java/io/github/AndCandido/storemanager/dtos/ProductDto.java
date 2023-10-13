package io.github.AndCandido.storemanager.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record ProductDto(
        @NotBlank(message = "{product.field.name.blank}")
        @Length(max = 50, message = "{product.field.name.max}")
        String name,
        String description,
        @NotNull(message = "{product.field.price.null}")
        BigDecimal price,
        @NotNull(message = "{product.field.stockQuantity.null}")
        Integer stockQuantity,
        @NotNull(message = "{product.field.code.null}")
        Integer code,
        Integer ref
) {
}
