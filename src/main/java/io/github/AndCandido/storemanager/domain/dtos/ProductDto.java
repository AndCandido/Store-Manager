package io.github.AndCandido.storemanager.domain.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record ProductDto(
        @NotBlank(message = "{product.field.name.blank}")
        @Length(max = 50, message = "{product.field.name.max}")
        String name,

        @NotNull(message = "{product.field.price.null}")
        @Min(value = 0, message = "{product.field.price.min}")
        BigDecimal price,

        @NotNull(message = "{product.field.stockQuantity.null}")
        @Min(value = 0, message = "{product.field.stockQuantity.min}")
        Integer stockQuantity
) {
}
