//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fjordtek.bookstore.service.HttpServerLogger;

/**
 *
 * This class implements Spring Framework security AccessDeniedHandler
 * interface with specific method overrides.
 * <p>
 * Main purpose is to properly handle erroneous client requests.
 *
 * @author Pekka Helenius
 */

public class BookStoreAccessDeniedHandler implements AccessDeniedHandler {

	private HttpServerLogger httpServerLogger = new HttpServerLogger();

	@Override
	public void handle(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			AccessDeniedException accessDeniedException
			) throws IOException, ServletException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			responseData.setStatus(HttpServletResponse.SC_FORBIDDEN);
			httpServerLogger.log(requestData, responseData);
		}

		responseData.sendRedirect("/");
	}

}