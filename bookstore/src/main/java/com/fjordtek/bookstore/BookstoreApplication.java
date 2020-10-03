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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fjordtek.bookstore.model.auth.Role;
import com.fjordtek.bookstore.model.auth.RoleRepository;
import com.fjordtek.bookstore.model.auth.User;
import com.fjordtek.bookstore.model.auth.UserRepository;
import com.fjordtek.bookstore.model.auth.UserRole;
import com.fjordtek.bookstore.model.auth.UserRoleRepository;
import com.fjordtek.bookstore.model.book.Author;
import com.fjordtek.bookstore.model.book.AuthorRepository;
import com.fjordtek.bookstore.model.book.Book;
import com.fjordtek.bookstore.model.book.BookHash;
import com.fjordtek.bookstore.model.book.BookHashRepository;
import com.fjordtek.bookstore.model.book.BookRepository;
import com.fjordtek.bookstore.model.book.Category;
import com.fjordtek.bookstore.model.book.CategoryRepository;

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
	private static final Logger commonLogger = LoggerFactory.getLogger(BookstoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner userDatabaseRunner(
			UserRepository userRepository,
			RoleRepository roleRepository,
			UserRoleRepository userRoleRepository
			) {

		return (args) -> {

			PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

			commonLogger.info("Add new roles to the database");
			Role adminAR    = new Role("ADMIN");
			Role helpdeskAR = new Role("HELPDESK");
			Role userAR     = new Role("USER");
			Role salesAR    = new Role("MARKETING");

			roleRepository.save(adminAR);
			roleRepository.save(helpdeskAR);
			roleRepository.save(userAR);
			roleRepository.save(salesAR);

			commonLogger.info("Add new users to database");

			User adminAU = new User(
					"admin",
					passwordEncoder.encode("admin"),
					"admin@fjordtek.com"
					);

			User helpdeskAU = new User(
					"helpdesk",
					passwordEncoder.encode("helpdesk"),
					"helpdesk@fjordtek.com"
					);

			User salesManagerAU = new User(
					"salesmanager",
					passwordEncoder.encode("salesmanager"),
					"salesmanager@fjordtek.com"
					);

			User userAU = new User(
					"user",
					passwordEncoder.encode("user"),
					"user@fjordtek.com"
					);

			userRepository.save(adminAU);
			userRepository.save(helpdeskAU);
			userRepository.save(userAU);
			userRepository.save(salesManagerAU);

			// Add example roles for admin user
			userRoleRepository.save(new UserRole(adminAU, adminAR));
			userRoleRepository.save(new UserRole(adminAU, salesAR));
			userRoleRepository.save(new UserRole(adminAU, userAR));

			// Add an example role for helpdesk user
			userRoleRepository.save(new UserRole(helpdeskAU, helpdeskAR));

			// Add an example role for sales manager
			userRoleRepository.save(new UserRole(salesManagerAU, salesAR));

			// Add an example role for user
			userRoleRepository.save(new UserRole(userAU, userAR));

			commonLogger.info("Sample roles in the database");
			for (Role role : roleRepository.findAll()) {
				commonLogger.info(role.toString());
			}
			commonLogger.info("Sample users in the database");
			commonLogger.info("**ENCRYPTED PASSWORDS ARE PRINTED ONLY FOR DEMO PURPOSES**");
			for (User user : userRepository.findAll()) {
				commonLogger.info(user.toString());
			}
			commonLogger.info("Sample user roles in the database");
			for (UserRole userRole : userRoleRepository.findAll()) {
				commonLogger.info(userRole.toString());
			}

		};
	}

	@Bean
	public CommandLineRunner bookDatabaseRunner(
			BookRepository bookRepository,
			BookHashRepository bookHashRepository,
			CategoryRepository categoryRepository,
			AuthorRepository authorRepository
			) {

		return (args) -> {

			commonLogger.info("Add new categories to the database");
			categoryRepository.save(new Category("Horror"));
			categoryRepository.save(new Category("Fantasy"));
			categoryRepository.save(new Category("Sci-Fi"));

			commonLogger.info("Add new authors to the database");
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
					categoryRepository.findByName("Horror").get(0),
					1
					);

			Book bookB = new Book(
					"The Witcher: The Lady of the Lake",
					authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
							"Andrzej","Sapkowski"
							).get(0),
					1999,
					"3213221-3",
					new BigDecimal("19.99"),
					categoryRepository.findByName("Fantasy").get(0),
					1
					);

			commonLogger.info("Add new sample books to the database");

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
