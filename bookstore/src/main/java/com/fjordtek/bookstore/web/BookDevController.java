//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fjordtek.bookstore.service.HttpServerLogger;

/**
 *
 * This class implements a Spring Model View controller for bookstore development
 * end points.
 *
 * @author Pekka Helenius
 */

@Controller
@RequestMapping("${page.url.dev}")
@Profile("dev")
public class BookDevController {

	@Autowired
	private Environment env;

	@Autowired
	private HttpServerLogger     httpServerLogger;

	//////////////////////////////
	// SPRING ACTUATOR HELP PAGE
	@RequestMapping(
			value    = "${page.url.dev.statsref}",
			method   = { RequestMethod.GET }
			)
	public String webFormActuatorRef(
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {
		httpServerLogger.log(requestData, responseData);
		return env.getProperty("page.url.dev.statsref");
	}

}

