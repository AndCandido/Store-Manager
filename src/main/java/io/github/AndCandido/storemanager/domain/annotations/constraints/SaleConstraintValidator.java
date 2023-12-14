package io.github.AndCandido.storemanager.domain.annotations.constraints;

import io.github.AndCandido.storemanager.domain.annotations.ValidateSaleDto;
import io.github.AndCandido.storemanager.domain.annotations.enums.SaleDtoFieldsValidator;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SaleConstraintValidator implements ConstraintValidator<ValidateSaleDto, SaleDto> {
    private SaleDtoFieldsValidator validator;

    @Override
    public void initialize(ValidateSaleDto constraintAnnotation) {
        this.validator = constraintAnnotation.fieldsValidator();
    }

    @Override
    public boolean isValid(SaleDto saleDto, ConstraintValidatorContext constraintValidatorContext) {
        return validator.validate(saleDto);
    }
}
