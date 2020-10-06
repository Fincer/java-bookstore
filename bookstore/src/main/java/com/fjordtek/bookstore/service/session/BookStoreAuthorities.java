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
* database authorities.
* <p>
* The attribute values of this class are primarily used,
* for instance, within @PreAuthorize annotations and
* in Thymeleaf HTML template files.
*
* @author Pekka Helenius
*/

@Component("BookAuth")
public class BookStoreAuthorities {

	@Autowired
	private Environment env;

	public String
	ADMIN,
	HELPDESK,
	SALES,
	USER
	;

	@PostConstruct
	private void constructAuthorities() {
		this.ADMIN    = env.getProperty("auth.authority.admin");
		this.HELPDESK = env.getProperty("auth.authority.helpdesk");
		this.SALES    = env.getProperty("auth.authority.sales");
		this.USER     = env.getProperty("auth.authority.user");
	}

}


