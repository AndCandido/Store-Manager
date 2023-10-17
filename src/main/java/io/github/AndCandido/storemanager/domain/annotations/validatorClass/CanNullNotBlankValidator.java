package io.github.AndCandido.storemanager.domain.annotations.validatorClass;

import io.github.AndCandido.storemanager.domain.annotations.CanNullNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CanNullNotBlankValidator implements ConstraintValidator<CanNullNotBlank, CharSequence> {

    @Override
    public void initialize(CanNullNotBlank constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CharSequence c, ConstraintValidatorContext constraintValidatorContext) {
        if(c == null) {
            return true;
        }
        return c.toString().trim().length() > 0;
    }
}
