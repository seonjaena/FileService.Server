package com.dau.file.validator.constraint;

import com.dau.file.validator.validator.SumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SumValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SumConstraint {
    String message() default "";
    String max() default "2147483647";
    String min() default "-2147483648";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
