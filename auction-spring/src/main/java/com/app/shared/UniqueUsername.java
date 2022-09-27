package com.app.shared;


import static java.lang.annotation.ElementType.FIELD;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = { UniqueUsernameValidator.class}) //for validation processes
public @interface UniqueUsername {
	String message() default "Invalid username. "; //error message will be return

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
