//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service.session;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
*
* This class gets Spring Environment key property values from
* Spring property sources and inserts them into publicly
* accessible class attributes. Attributes are related to
* application web pages.
* <p>
* The attribute values of this class are primarily used
* in Thymeleaf HTML template files.
*
* @author Pekka Helenius
*/

@Component("Pages")
public class BookStorePages {

	@Autowired
	private Environment env;

	public String
	INDEX,
	LIST,
	ADD,
	DELETE,
	EDIT,
	LOGIN,
	LOGOUT,
	ERROR,
	AUTHERROR,
	APIREF,
	JSON,
	JSONBOOK,
	JSONLIST,
	ACTUATOR,
	DEV,
	DEVSTATSREF,
	H2CONSOLE,
	REST,
	RESTAUTHORS,
	RESTBOOKS,
	RESTCATEGORIES,
	RESTUSERS,
	RESTROLES,
	RESTUSERROLES,
	RESCSS,
	RESJS,
	RESIMAGES
	;

	@PostConstruct
	private void constructPages() {
		this.INDEX          = env.getProperty("page.url.index");
		this.LIST           = env.getProperty("page.url.list");
		this.ADD            = env.getProperty("page.url.add");
		this.DELETE         = env.getProperty("page.url.delete");
		this.EDIT           = env.getProperty("page.url.edit");

		this.LOGIN          = env.getProperty("page.url.login");
		this.LOGOUT         = env.getProperty("page.url.logout");

		this.ERROR          = env.getProperty("page.url.error");
		this.AUTHERROR      = env.getProperty("page.url.autherror");

		this.APIREF         = env.getProperty("page.url.apiref");

		this.JSON           = env.getProperty("page.url.json");
		this.JSONBOOK       = env.getProperty("page.url.json.book");
		this.JSONLIST       = env.getProperty("page.url.json.list");

		this.ACTUATOR       = env.getProperty("page.url.actuator");

		this.DEV            = env.getProperty("page.url.dev");
		this.DEVSTATSREF    = env.getProperty("page.url.dev.statsref");

		this.H2CONSOLE      = env.getProperty("spring.h2.console.path");

		this.REST           = env.getProperty("spring.data.rest.base-path");
		this.RESTAUTHORS    = env.getProperty("page.url.restapi.authors");
		this.RESTBOOKS      = env.getProperty("page.url.restapi.books");
		this.RESTCATEGORIES = env.getProperty("page.url.restapi.categories");
		this.RESTUSERS      = env.getProperty("page.url.restapi.users");
		this.RESTROLES      = env.getProperty("page.url.restapi.roles");
		this.RESTUSERROLES  = env.getProperty("page.url.restapi.userroles");

		this.RESCSS         = env.getProperty("page.url.resources.css");
		this.RESJS          = env.getProperty("page.url.resources.js");
		this.RESIMAGES      = env.getProperty("page.url.resources.images");

	}

}


