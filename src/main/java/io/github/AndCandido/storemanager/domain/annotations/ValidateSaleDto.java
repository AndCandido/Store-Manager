package io.github.AndCandido.storemanager.domain.annotations;

import io.github.AndCandido.storemanager.domain.annotations.constraints.SaleConstraintValidator;
import io.github.AndCandido.storemanager.domain.annotations.enums.SaleDtoFieldsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SaleConstraintValidator.class)
public @interface ValidateSaleDto {
    String message() default "";

    SaleDtoFieldsValidator fieldsValidator();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValidateSaleDto[] value();
    }
}
