//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.annotation;

import java.time.Year;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class CurrentYearValidator implements ConstraintValidator<CurrentYear, Integer> {

	private static final int yearNow = Year.now().getValue();

	@Override
	public void initialize(CurrentYear yearAnnotation) {
	}

	@Override
	public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
		return year <= yearNow;
	}
}