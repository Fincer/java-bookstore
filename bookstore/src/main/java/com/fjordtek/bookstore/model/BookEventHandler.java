//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Book.class)
public class BookEventHandler {

	@Autowired
	private BookHashRepository bookHashRepository;

	/*
	 * When using REST API to add a new book, we need to add a corresponding
	 * book hash Id to BOOK_HASH table, as well.
	 *
	 * For instance, this is a case with the following alike curl commands:
	 *     curl --request POST --header "Content-Type: application/json" --data
	 *     '{"title":"Bloody Chamber", ...}' http://localhost:8080/api/booklist
	 *
	 * Ref: https://docs.spring.io/spring-data/rest/docs/current/reference/html/#events
	 *      https://stackoverflow.com/questions/49504103/override-spring-data-rest-post-method
	 *      https://www.baeldung.com/spring-data-rest-events
	 */
	@HandleAfterCreate
	public void handleAfterCreate(Book book) {
		BookHash bookHash = new BookHash();
		book.setBookHash(bookHash);
		bookHash.setBook(book);

		bookHashRepository.save(bookHash);
	}
}