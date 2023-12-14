package io.github.AndCandido.storemanager.domain.annotations.constraints;

import io.github.AndCandido.storemanager.domain.annotations.ValidateInstallmentDto;
import io.github.AndCandido.storemanager.domain.annotations.enums.InstallmentDtoFieldsValidator;
import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstallmentConstraintValidator implements ConstraintValidator<ValidateInstallmentDto, InstallmentDto> {

    private InstallmentDtoFieldsValidator validator;

    @Override
    public void initialize(ValidateInstallmentDto constraintAnnotation) {
        this.validator = constraintAnnotation.fieldsValidator();
    }

    @Override
    public boolean isValid(InstallmentDto installmentsDto, ConstraintValidatorContext constraintValidatorContext) {
        return validator.validate(installmentsDto);
    }
}
