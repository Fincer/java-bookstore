//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service.session;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fjordtek.bookstore.model.book.BookRepository;

/**
 *
 * This service class is implemented due to major
 * security risks we encounter when publishing this
 * application on the Internet <b>without any</b>
 * user account restrictions, as this was intended
 * and required for application demonstration purposes.
 *
 * @author Pekka Helenius
 *
 */

@Service
public class BookStoreWebRestrictions {

	@Autowired
	private Environment env;

	@Autowired
	private BookRepository bookRepository;

	private boolean areRestrictionsEnabled(String profile) {

		if ( Arrays.stream(env.getActiveProfiles()).anyMatch(p -> p.contains(profile)) ) {
			if (Boolean.parseBoolean(env.getProperty("security.enabled"))) {
				return true;
			}
		}
		return false;
	}

	public boolean limitBookMaxCount(String profile) {

		if (areRestrictionsEnabled(profile)) {

			if (env.getProperty("security.book.count.max") != null) {

				try {
					int bookCountMax = Integer.parseInt(env.getProperty("security.book.count.max"));

					if (bookRepository.count() == bookCountMax) {
						return true;
					}

				} catch (NumberFormatException e) {
					// TODO logging
				}

			}

		}

		return false;
	}

}