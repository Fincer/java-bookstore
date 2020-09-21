//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The annotated element must be a year number equal or less than the current year.
 *
 * @author Pekka Helenius
 */
@Documented
@Constraint(validatedBy = CurrentYearValidator.class)
@Target(
		value={
				ElementType.TYPE_USE,
				ElementType.ANNOTATION_TYPE,
				ElementType.CONSTRUCTOR,
				ElementType.FIELD,
				ElementType.METHOD,
				ElementType.PARAMETER
		}
		)
@Retention(RetentionPolicy.RUNTIME)

public @interface CurrentYear {
	String message() default "Year must not be in the future";
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };

}