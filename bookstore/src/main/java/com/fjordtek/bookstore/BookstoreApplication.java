package com.fjordtek.bookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.fjordtek.bookstore.model.BookRepository;

import com.fjordtek.bookstore.model.*;

@SpringBootApplication
public class BookstoreApplication extends SpringBootServletInitializer {
	private static final Logger commonLogger = LoggerFactory.getLogger(BookstoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner bookDatabaseRunner(BookRepository repository) {
		
		return (args) -> {
			commonLogger.info("Add new sample books to database");
		
			repository.save(new Book("Book 1 title", "Book 1 author", 2020, "aaa-b2b-c3c-444", 40.00));
			repository.save(new Book("Book 2 title", "Book 2 author", 2005, "111-2b2-3c3-ddd", 20.17));

			commonLogger.info("------------------------------");
			commonLogger.info("Sample books in the database");
			for (Book book : repository.findAll()) {
				commonLogger.info(book.toString());
			}

		};
	}
	
}
