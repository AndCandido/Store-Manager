package io.github.AndCandido.storemanager.domain.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Builder
public record ProductRequestDto(

    @NotBlank(message = "{product.field.name.blank}")
    @Length(max = 50, message = "{product.field.name.max}")
    String name,

    @NotNull(message = "{product.field.ref.null}")
    @Min(value = 0, message = "{product.field.ref.min}")
    Long ref,

    @Valid
    List<ProductSoldRequestDto> productsSold,

    @NotNull(message = "{product.field.price.null}")
    @Min(value = 0, message = "{product.field.price.min}")
    Double price,

    @NotNull(message = "{product.field.stockQuantity.null}")
    @Min(value = 0, message = "{product.field.stockQuantity.min}")
    Integer stockQuantity
) {
}
