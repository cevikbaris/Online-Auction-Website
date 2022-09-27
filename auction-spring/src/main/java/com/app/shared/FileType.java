package com.app.shared;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target({FIELD }) 
@Retention(RUNTIME)
@Constraint(validatedBy = { FileTypeValidator.class})
public @interface FileType {

	String message() default "Image must be jpeg or png.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	String[] types();//birden fazla dosya türünü parametre yollamak içn
}
