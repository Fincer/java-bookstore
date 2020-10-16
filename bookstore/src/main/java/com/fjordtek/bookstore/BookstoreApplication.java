// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

/**
*
* This is the main Spring Boot application class for the bookstore project.
* <p>
* Initializes and handles initialization of the application.
*
* @author Pekka Helenius
*/

@SpringBootApplication
/*@SpringBootApplication(
	exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
)*/
public class BookstoreApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public UrlTemplateResolver urlTemplateResolver() {
		UrlTemplateResolver urlTemplateResolver = new UrlTemplateResolver();
		urlTemplateResolver.setCacheable(true);

		// TTL value in milliseconds
//		urlTemplateResolver.setCacheTTLMs(120000L);

		urlTemplateResolver.getCharacterEncoding();
		return urlTemplateResolver;
	}

}
