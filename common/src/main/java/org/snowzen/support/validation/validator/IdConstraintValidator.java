package org.snowzen.support.validation.validator;

import org.snowzen.support.validation.constraints.ValidId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author snow-zen
 */
public class IdConstraintValidator implements ConstraintValidator<ValidId, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return Integer.signum(value) > 0;
    }
}
