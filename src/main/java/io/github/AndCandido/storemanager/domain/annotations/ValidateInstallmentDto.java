package io.github.AndCandido.storemanager.domain.annotations;

import io.github.AndCandido.storemanager.domain.annotations.constraints.InstallmentConstraintValidator;
import io.github.AndCandido.storemanager.domain.annotations.enums.InstallmentDtoFieldsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InstallmentConstraintValidator.class)
public @interface ValidateInstallmentDto {

    String message() default "";

    InstallmentDtoFieldsValidator fieldsValidator();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValidateInstallmentDto[] value();
    }
}
