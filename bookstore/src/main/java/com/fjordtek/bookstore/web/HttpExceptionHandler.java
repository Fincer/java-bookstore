// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HttpExceptionHandler {

	private static final String HTTPNOTFOUND  = "Invalid request";
	private HttpServerLogger httpServerLogger = new HttpServerLogger();

	@ResponseStatus(
			value   = HttpStatus.NOT_FOUND,
			reason  = HTTPNOTFOUND
			)
	// Very simple exception handler (not any sophistication)
	@ExceptionHandler(Exception.class)
	public String notFoundErrorHandler(HttpServletRequest requestData) {

		httpServerLogger.logMessageError(
				requestData,
				"HTTPNOTFOUND"
				);
		return "error";
	}

}
