//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fjordtek.bookstore.model.Author;
import com.fjordtek.bookstore.model.AuthorRepository;
import com.fjordtek.bookstore.model.Book;

/**
*
* This class implements special methods for handling book-related author
* saving and updating operations.
* <p>
* Since the bookstore implements a separate AUTHOR table for BOOK with
* a relation between both, sophisticated methods are required in
* some controller operations.
*
* @author Pekka Helenius
*/

@Component
public class BookAuthorHelper {

	@Autowired
	private AuthorRepository authorRepository;

	public void detectAndSaveUpdateAuthorForBook(Book book) {

		/*
		 * Find an author from the current AUTHOR table by his/her first and last name,
		 * included in the Book entity object. In CrudRepository, if a matching Id
		 * value is not found, it is stored as a new Author entity object. Therefore,
		 * it's crucial to identify whether author row values already exist in
		 * AUTHOR table.
		 */

		try {
			Author authorI = authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
					book.getAuthor().getFirstName(),book.getAuthor().getLastName())
					.get(0);

			/*
			 * When author is found, use it's Id attribute for book's author Id
			 *
			 * NOTE: This step is required by Hibernate, otherwise we get
			 * TransientPropertyValueException
			 */
			book.getAuthor().setId(authorI.getId());

			/*
			 * Otherwise, consider this a new author and store it appropriately.
			 * Actually, when author is not found, we get IndexOutOfBoundsException.
			 */
		} catch (IndexOutOfBoundsException e) {
			authorRepository.save(book.getAuthor());
		}

	}

	public Author detectAndSaveAuthorByName(String firstName, String lastName) {

		/*
		 * Find an author from the current AUTHOR table by his/her first and last name.
		 * In CrudRepository, if a matching Id value is not found, it is stored
		 * as a new Author entity object. Therefore, it's crucial to identify
		 * whether author row values already exist in AUTHOR table.
		 */

		try {
			return authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
					firstName,lastName).get(0);
			/*
			 * Otherwise, consider this a new author and store it appropriately.
			 * Actually, when author is not found, we get IndexOutOfBoundsException.
			 */
		} catch (IndexOutOfBoundsException e) {
			return authorRepository.save(new Author(firstName,lastName));
		}

	}

	// TODO implement methods to save author even if either his/her first or last name is missing

}