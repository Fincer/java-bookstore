// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.fjordtek.bookstore.model.Book;
import com.fjordtek.bookstore.model.BookRepository;
import com.fjordtek.bookstore.model.Category;
import com.fjordtek.bookstore.model.CategoryRepository;

@SpringBootApplication
public class BookstoreApplication extends SpringBootServletInitializer {
	private static final Logger commonLogger = LoggerFactory.getLogger(BookstoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner bookDatabaseRunner(BookRepository bookRepository, CategoryRepository categoryRepository) {

		return (args) -> {

			commonLogger.info("Add new categories to database");

			Category horror  = new Category("Horror");
			Category fantasy = new Category("Fantasy");
			categoryRepository.save(horror);
			categoryRepository.save(fantasy);

			commonLogger.info("Add new sample books to database");

			bookRepository.save(new Book("Book 1 title", "Book 1 author", 2020, "1231231-12", 40.00, horror));
			bookRepository.save(new Book("Book 2 title", "Book 2 author", 2005, "3213221-3",  20.17, fantasy));

			commonLogger.info("------------------------------");
			commonLogger.info("Sample categories in the database");
			for (Category category : categoryRepository.findAll()) {
				commonLogger.info(category.toString());
			}
			commonLogger.info("Sample books in the database");
			for (Book book : bookRepository.findAll()) {
				commonLogger.info(book.toString());
			}

		};
	}

}
