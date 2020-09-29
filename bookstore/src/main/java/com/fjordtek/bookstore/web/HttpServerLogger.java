// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
* This class implements a custom logger for HTTP requests.
*
* @author Pekka Helenius
*/

public class HttpServerLogger {

	private static final Logger serverLogger = LoggerFactory.getLogger(HttpServerLogger.class);

	private static boolean httpStatusRange(int comparable, int lowLimit, int upLimit) {
		return lowLimit <= comparable && comparable <= upLimit;
	}

	public void log(
			HttpServletRequest request,
			HttpServletResponse response
			) {

		int status = response.getStatus();

		List<String> requestParams            = new ArrayList<String>();
		Enumeration<String> requestParamNames = request.getParameterNames();

		if (requestParamNames != null) {
			while (requestParamNames.hasMoreElements()) {

				String paramName     = requestParamNames.nextElement().toString();
				String[] paramValues = request.getParameterValues(paramName);

				requestParams.add(
						paramName + " = " +
						String.join(", ", paramValues)
						);
			}
		}

		if (httpStatusRange(status, 0, 399)) {
			serverLogger.info(
					"HTTP request to '{}' from client '{}' [status: {}, attributes: {}]",
					request.getRequestURL(),
					request.getRemoteAddr(),
					response.getStatus(),
					requestParams
					);
		} else {
			serverLogger.error(
					"Invalid HTTP request to '{}' from client '{}' [status: {}, attributes: {}]",
					request.getRequestURL(),
					request.getRemoteAddr(),
					response.getStatus(),
					requestParams
					);
		}

	}

	public void commonError(
			String errorMsg,
			HttpServletRequest request,
			HttpServletResponse response,
			String ...strings) {

		String[] dataString = strings;

		serverLogger.error(
				"{} - {} [status: {}]",
				request.getRemoteAddr(),
				errorMsg,
				response.getStatus()
				);
		if (dataString != null) {
			serverLogger.error("{}", String.join(", ", dataString));
		}
	}

}
