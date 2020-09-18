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

			categoryRepository.save(new Category("Horror"));
			categoryRepository.save(new Category("Fantasy"));
			categoryRepository.save(new Category("Sci-Fi"));

			commonLogger.info("Add new sample books to database");

			bookRepository.save(new Book(
					"Bloody Chamber",
					"Angela Carter",
					1979,
					"1231231-12",
					new BigDecimal("18.00"),
					categoryRepository.findByName("Horror").get(0)
					));
			bookRepository.save(new Book(
					"The Witcher: The Lady of the Lake",
					"Andrzej Sapkowski",
					1999,
					"3213221-3",
					new BigDecimal("19.99"),
					categoryRepository.findByName("Fantasy").get(0)
					));

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
