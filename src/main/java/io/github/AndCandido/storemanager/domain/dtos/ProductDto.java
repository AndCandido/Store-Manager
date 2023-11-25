package io.github.AndCandido.storemanager.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProductDto(
        Long id,

        @NotBlank(message = "{product.field.name.blank}")
        @Length(max = 50, message = "{product.field.name.max}")
        String name,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Valid
        List<ProductSoldDto> productsSold,

        @NotNull(message = "{product.field.price.null}")
        @Min(value = 0, message = "{product.field.price.min}")
        BigDecimal price,

        @NotNull(message = "{product.field.stockQuantity.null}")
        @Min(value = 0, message = "{product.field.stockQuantity.min}")
        Integer stockQuantity,

        LocalDateTime createdAt
) {
}
