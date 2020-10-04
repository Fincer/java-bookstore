//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
*
* This class implements Spring Framework security AuthenticationSuccessHandler
* interface with specific method overrides.
* <p>
* Main purpose is to properly handle valid authentication requests.
*
* @author Pekka Helenius
*/

public class BookStoreAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Authentication authentication
			) throws IOException, ServletException {

		// Nothing special here
		// TODO add proper server logging for auditing purposes

		redirectStrategy.sendRedirect(requestData, responseData, "/");
		//responseData.sendRedirect("/");

	}

}