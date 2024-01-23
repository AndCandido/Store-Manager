package io.github.AndCandido.storemanager.domain.annotations.constraints;

import io.github.AndCandido.storemanager.domain.annotations.ValidateInstallmentDto;
import io.github.AndCandido.storemanager.domain.annotations.enums.InstallmentDtoFieldsValidator;
import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstallmentConstraintValidator implements ConstraintValidator<ValidateInstallmentDto, InstallmentRequestDto> {

    private InstallmentDtoFieldsValidator validator;

    @Override
    public void initialize(ValidateInstallmentDto constraintAnnotation) {
        this.validator = constraintAnnotation.fieldsValidator();
    }

    @Override
    public boolean isValid(InstallmentRequestDto installmentsDto, ConstraintValidatorContext constraintValidatorContext) {
        return validator.validate(installmentsDto);
    }
}
