//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjordtek.bookstore.model.book.Author;
import com.fjordtek.bookstore.model.book.AuthorRepository;
import com.fjordtek.bookstore.model.book.Book;

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

@Service
public class BookAuthorHelper {

	@Autowired
	private AuthorRepository authorRepository;

	public Author detectAndSaveUpdateAuthorByName(String firstName, String lastName) {

		/*
		 * Find an author from the current AUTHOR table by his/her first and/or last
		 * name. In CrudRepository, if a matching Id value is not found, it is stored
		 * as a new Author entity object. Therefore, it's crucial to identify whether
		 * author row values already exist in AUTHOR table.
		 */

		Author author = null;

		try {

			/*
			 * Find by first and last name; ignore case
			 */
			if (firstName != null && lastName != null) {
				author = authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
					firstName,lastName).get(0);
			}

			/*
			 * Find by first name; ignore case
			 * NOTE: There is a risk we get a wrong Author!
			 * Should we auto-detect author or let the user absolutely
			 * decide the naming scheme without 'intelligent' detection feature?
			 */
			if (firstName != null && lastName == null) {
				author = authorRepository.findByFirstNameIgnoreCaseContaining(
						firstName).get(0);
			}

			/*
			 * Find by last name; ignore case
			 * NOTE: There is a risk we get a wrong Author!
			 * Should we auto-detect author or let the user absolutely
			 * decide the naming scheme without 'intelligent' detection feature?
			 */
			if (firstName == null && lastName != null) {
				author = authorRepository.findByLastNameIgnoreCaseContaining(
						lastName).get(0);
			}

		/*
		 * If author is still not found, we get IndexOutOfBoundsException
		 * Consider this a new author and store it appropriately.
		 */
		} catch (IndexOutOfBoundsException e) {
			author = authorRepository.save(new Author(firstName, lastName));
		}

		return author;
	}

	public void detectAndSaveUpdateAuthorForBook(Book book) {

		Author author = this.detectAndSaveUpdateAuthorByName(
				book.getAuthor().getFirstName(),
				book.getAuthor().getLastName()
				);

		/*
		 * When author is found, use it's Id attribute for book's author Id
		 *
		 * NOTE: This step is required by Hibernate,
		 * otherwise we get TransientPropertyValueException
		 */
		if (author != null) {
			book.getAuthor().setId(author.getId());
		} else {
			book.setAuthor(null);
		}

	}

}