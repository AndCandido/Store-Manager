package io.github.AndCandido.storemanager.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.AndCandido.storemanager.domain.dtos.groups.RequestGroup;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProductDto(

    @Null(message = "{field.id.null}", groups = RequestGroup.class)
    Long id,

    @NotBlank(message = "{product.field.name.blank}", groups = RequestGroup.class)
    @Length(max = 50, message = "{product.field.name.max}", groups = RequestGroup.class)
    String name,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Validated(RequestGroup.class)
    List<ProductSoldDto> productsSold,

    @NotNull(message = "{product.field.price.null}", groups = RequestGroup.class)
    @Min(value = 0, message = "{product.field.price.min}", groups = RequestGroup.class)
    Double price,

    @NotNull(message = "{product.field.stockQuantity.null}", groups = RequestGroup.class)
    @Min(value = 0, message = "{product.field.stockQuantity.min}", groups = RequestGroup.class)
    Integer stockQuantity,

    @Null(message = "{field.createdAt.null}", groups = RequestGroup.class)
    LocalDateTime createdAt
) {
}
