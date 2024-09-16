package com.dau.file.validator.constraint;

import com.dau.file.validator.validator.UserCntValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserCntValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserCntConstraint {
    String message() default "";
    int max() default Integer.MAX_VALUE;
    int min() default Integer.MIN_VALUE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
