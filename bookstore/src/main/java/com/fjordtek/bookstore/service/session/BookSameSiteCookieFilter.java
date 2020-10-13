//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service.session;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @see https://owasp.org/www-community/SameSite
 * @see https://docs.microsoft.com/en-us/previous-versions//ms533046%28v=vs.85%29?redirectedfrom=MSDN#protecting-data-with-http-only-cookies
 *
 * @author Pekka Helenius
 */

public class BookSameSiteCookieFilter extends GenericFilterBean {

	@Override
	public void doFilter(
			ServletRequest requestData,
			ServletResponse responseData,
			FilterChain chain)
			throws IOException, ServletException {

			HttpServletResponse httpResponse = (HttpServletResponse) responseData;
			/*
			 * Unnecessary, already set 'HttpOnly' cookie string removed.
			 * Unit tests do not like it (colon separation maybe?)
			 */
			httpResponse.setHeader("Set-Cookie", "SameSite=strict;");
			chain.doFilter(requestData, responseData);

	}


}