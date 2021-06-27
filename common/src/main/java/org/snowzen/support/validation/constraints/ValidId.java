package org.snowzen.support.validation.constraints;

import org.snowzen.support.validation.validator.IdConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author snow-zen
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IdConstraintValidator.class})
public @interface ValidId {

    String message() default "{org.snowzen.todo.valid.ValidId.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
