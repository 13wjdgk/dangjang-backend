package com.coniverse.dangjang.global.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateValidator.class)
public @interface LocalDateValid {
	String message() default "유효하지 않은 날짜입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
