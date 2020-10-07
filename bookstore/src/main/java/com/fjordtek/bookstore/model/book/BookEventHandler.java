//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * This class is part of Spring framework, having @Component
 * annotation.
 * <p>
 * The class implements @RepositoryEventHandler annotation
 * for Book class which instructs Spring to use custom method
 * implementations for events annotated with specific event
 * handlers in the class.
 *
 * @see https://docs.spring.io/spring-data/rest/docs/current/reference/html/#events
 * @see https://stackoverflow.com/questions/49504103/override-spring-data-rest-post-method
 * @see https://www.baeldung.com/spring-data-rest-events
 *
 * @author Pekka Helenius
 */

@Component
@RepositoryEventHandler(Book.class)
public class BookEventHandler {

	@Autowired
	private BookHashRepository bookHashRepository;

	/*
	 * Generate hash id for the book. One-to-one unidirectional tables.
	 * Associate generated book hash object information
	 * to the book (table).
	 * Associate new book object information
	 * to the book hash (table).
	 *
	 * When using REST API to add a new book, we need to add a corresponding
	 * book hash Id to BOOK_HASH table, as well.
	 *
	 * For instance, this is a case with the following alike curl commands:
	 *     curl --request POST --header "Content-Type: application/json" --data
	 *     '{"title":"Bloody Chamber", ...}' http://localhost:8080/api/booklist
	 *
	 */
	@HandleAfterCreate
	public void handleAfterCreate(Book book) {
		BookHash bookHash = new BookHash();

		/*
		 * In a very unlikely scenario, we have hash collisions
		 * (same hash values generated for two different book entity objects).
		 * Therefore, we need to check that hash id is not already defined for
		 * some other book.
		 */
		int i = 0;
		while (i < 5) {
			if (bookHashRepository.findByHashId(bookHash.getHashId()) != null) {
				bookHash = new BookHash();
			} else {
				break;
			}
			i++;
		}

		book.setBookHash(bookHash);
		bookHash.setBook(book);

		bookHashRepository.save(bookHash);
	}
}