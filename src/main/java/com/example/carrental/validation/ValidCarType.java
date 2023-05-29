package com.example.carrental.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CarTypeValidator.class)
@Documented
public @interface ValidCarType {
    String message() default "Invalid car type for given price range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}