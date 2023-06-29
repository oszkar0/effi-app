package com.effi.EffiApp.validators.deadline;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.sql.Date;
import java.time.LocalDate;

public class DateNotBeforeTodayValidator implements ConstraintValidator<DateNotBeforeToday, Date> {
    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return true; // Null values are handled by @NotNull annotation
        }

        LocalDate today = LocalDate.now();
        LocalDate formDate = date.toLocalDate();

        return !formDate.isBefore(today);
    }

    @Override
    public void initialize(DateNotBeforeToday constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
