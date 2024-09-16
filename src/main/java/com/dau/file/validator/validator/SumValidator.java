package com.dau.file.validator.validator;

import com.dau.file.validator.constraint.SumConstraint;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.math.BigDecimal;

@RequiredArgsConstructor
public class SumValidator implements ConstraintValidator<SumConstraint, BigDecimal> {

    private String messageCode;
    private final MessageSource messageSource;
    private BigDecimal maxValue;
    private BigDecimal minValue;
    private String errorMessage;

    @Override
    public void initialize(SumConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.messageCode = StringUtils.isBlank(constraintAnnotation.message()) ? "error.user-input.invalid" : constraintAnnotation.message();
        this.maxValue = new BigDecimal(constraintAnnotation.max());
        this.minValue = new BigDecimal(constraintAnnotation.min());
    }

    @Override
    public boolean isValid(BigDecimal sum, ConstraintValidatorContext context) {
        if(sum == null || sum.compareTo(maxValue) > 0 || sum.compareTo(minValue) < 0) {
            try {
                errorMessage = messageSource.getMessage(this.messageCode, new BigDecimal[]{sum}, LocaleContextHolder.getLocale());
            }catch(Exception e) {
                errorMessage = this.messageCode;
            }

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
