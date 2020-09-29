// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.fjordtek.bookstore.model.Author;
import com.fjordtek.bookstore.model.AuthorRepository;
import com.fjordtek.bookstore.model.Book;
import com.fjordtek.bookstore.model.BookHash;
import com.fjordtek.bookstore.model.BookHashRepository;
import com.fjordtek.bookstore.model.BookRepository;
import com.fjordtek.bookstore.model.Category;
import com.fjordtek.bookstore.model.CategoryRepository;

/**
*
* This is the main Spring Boot application class for the bookstore project.
* <p>
* Initializes and handles initialization of the application.
*
* @author Pekka Helenius
*/

@SpringBootApplication
public class BookstoreApplication extends SpringBootServletInitializer {
	private static final Logger commonLogger = LoggerFactory.getLogger(BookstoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner bookDatabaseRunner(
			BookRepository bookRepository,
			BookHashRepository bookHashRepository,
			CategoryRepository categoryRepository,
			AuthorRepository authorRepository
			) {

		return (args) -> {

			commonLogger.info("Add new categories to database");
			categoryRepository.save(new Category("Horror"));
			categoryRepository.save(new Category("Fantasy"));
			categoryRepository.save(new Category("Sci-Fi"));

			commonLogger.info("Add new authors to database");
			authorRepository.save(new Author("Angela","Carter"));
			authorRepository.save(new Author("Andrzej","Sapkowski"));

			Book bookA = new Book(
					"Bloody Chamber",
					authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
							"Angela","Carter"
							).get(0),
					1979,
					"1231231-12",
					new BigDecimal("18.00"),
					categoryRepository.findByName("Horror").get(0)
					);

			Book bookB = new Book(
					"The Witcher: The Lady of the Lake",
					authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
							"Andrzej","Sapkowski"
							).get(0),
					1999,
					"3213221-3",
					new BigDecimal("19.99"),
					categoryRepository.findByName("Fantasy").get(0)
					);

			commonLogger.info("Add new sample books to database");

			bookRepository.save(bookA);
			bookRepository.save(bookB);

			BookHash bookHashA = new BookHash();
			BookHash bookHashB = new BookHash();

			// One-to-one unidirectional relationship
			// Both directions for table operations must be considered here.
			bookA.setBookHash(bookHashA);
			bookB.setBookHash(bookHashB);
			bookHashA.setBook(bookA);
			bookHashB.setBook(bookB);

			bookHashRepository.save(bookHashA);
			bookHashRepository.save(bookHashB);

			commonLogger.info("------------------------------");
			commonLogger.info("Sample categories in the database");
			for (Category category : categoryRepository.findAll()) {
				commonLogger.info(category.toString());
			}
			commonLogger.info("Sample authors in the database");
			for (Author author : authorRepository.findAll()) {
				commonLogger.info(author.toString());
			}
			commonLogger.info("Sample books in the database");
			for (Book book : bookRepository.findAll()) {
				commonLogger.info(book.toString());
			}
			commonLogger.info("Sample book hashes in the database");
			commonLogger.info("**THIS IS ADDED FOR SECURITY PURPOSES**");
			for (BookHash hash : bookHashRepository.findAll()) {
				commonLogger.info(hash.toString());
			}

		};
	}

}
