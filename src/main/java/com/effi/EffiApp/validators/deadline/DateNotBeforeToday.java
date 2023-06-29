package com.effi.EffiApp.validators.deadline;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateNotBeforeTodayValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateNotBeforeToday {
    String message() default "Date must be after or equal today";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
