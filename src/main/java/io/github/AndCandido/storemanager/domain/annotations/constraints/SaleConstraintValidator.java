package io.github.AndCandido.storemanager.domain.annotations.constraints;

import io.github.AndCandido.storemanager.domain.annotations.ValidateSaleDto;
import io.github.AndCandido.storemanager.domain.annotations.enums.SaleDtoFieldsValidator;
import io.github.AndCandido.storemanager.domain.dtos.requests.SaleRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SaleConstraintValidator implements ConstraintValidator<ValidateSaleDto, SaleRequestDto> {
    private SaleDtoFieldsValidator validator;

    @Override
    public void initialize(ValidateSaleDto constraintAnnotation) {
        this.validator = constraintAnnotation.fieldsValidator();
    }

    @Override
    public boolean isValid(SaleRequestDto saleRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        return validator.validate(saleRequestDto);
    }
}
