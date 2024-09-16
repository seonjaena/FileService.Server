package com.dau.file.validator.validator;

import com.dau.file.validator.constraint.UserCntConstraint;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
public class UserCntValidator implements ConstraintValidator<UserCntConstraint, Integer> {

    private String messageCode;
    private final MessageSource messageSource;
    private int maxValue;
    private int minValue;
    private String errorMessage;

    @Override
    public void initialize(UserCntConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.messageCode = StringUtils.isBlank(constraintAnnotation.message()) ? "error.user-input.invalid" : constraintAnnotation.message();
        this.maxValue = constraintAnnotation.max();
        this.minValue = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Integer userCnt, ConstraintValidatorContext context) {
        if(userCnt == null || userCnt > maxValue || userCnt < minValue) {
            try {
                errorMessage = messageSource.getMessage(this.messageCode, new Integer[]{userCnt}, LocaleContextHolder.getLocale());
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
