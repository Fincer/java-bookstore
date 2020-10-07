//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fjordtek.bookstore.service.HttpServerLogger;

/**
*
* This class implements Spring Framework security AuthenticationFailureHandler
* interface with specific method overrides.
* <p>
* Main purpose is to properly handle invalid authentication requests.
* <p>
* Additional request attributes are being delivered to /autherror POST end point.
*
* @see com.fjordtek.bookstore.web.BookController
*
* @author Pekka Helenius
*/

public class BookStoreAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private Environment env;
	private MessageSource msg;

	private HttpServerLogger httpServerLogger = new HttpServerLogger();


	@Override
	public void onAuthenticationFailure(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			AuthenticationException exception
			) throws IOException, ServletException {

		responseData.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpServerLogger.log(requestData, responseData);

		String username = requestData.getParameter(env.getProperty("auth.field.username"));
		if (username.length() > 50) {
			username = username.substring(0, 50) + " ...";
		}

		requestData.setAttribute("username", username);

		requestData
			.setAttribute("authfailure", msg.getMessage(
					"page.auth.failure",
					null,
					"page.auth.failure [placeholder]",
					requestData.getLocale()
					));

		requestData.getRequestDispatcher(env.getProperty("page.url.autherror"))
			.forward(requestData, responseData);

	}

	////////////////////
	// Class constructors

	public BookStoreAuthenticationFailureHandler() {
	}

	/*
	 * Autowired annotation does not work.
	 * Therefore, pass Environment & MessageSource as parameters to this class constructor.
	 */
	public BookStoreAuthenticationFailureHandler(Environment env, MessageSource msg) {
		this.env = env;
		this.msg = msg;
	}

}
