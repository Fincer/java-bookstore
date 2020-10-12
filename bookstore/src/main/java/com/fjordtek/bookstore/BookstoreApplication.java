// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore;

import java.math.BigDecimal;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

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

	@Autowired
	private Environment env;

	@Bean
	public UrlTemplateResolver urlTemplateResolver() {
		UrlTemplateResolver urlTemplateResolver = new UrlTemplateResolver();
		urlTemplateResolver.setCacheable(true);

		// TTL value in milliseconds
//		urlTemplateResolver.setCacheTTLMs(120000L);

		urlTemplateResolver.getCharacterEncoding();
		return urlTemplateResolver;
	}

	@Bean
	@Profile({"dev", "prod"})
	public CommandLineRunner userDatabaseRunner(
			UserRepository userRepository,
			RoleRepository roleRepository,
			UserRoleRepository userRoleRepository
			) {

		return (args) -> {

			/*
			 * Set hash strength to 14 (2^14) + use RNG to randomize generated hash.
			 * Default strength value is 10.
			 */
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(14, new SecureRandom());

			//////////////////////////////
			commonLogger.info("Add new roles to the database");

			/*
			 * New and predefined roles.
			 *
			 * Role names are unique. We do not attempt to add duplicate entries
			 * This is an issue with traditional SQL databases if existing entries
			 * are present.
			 */

			Role adminAR    = new Role(env.getProperty("auth.authority.admin"));
			if (roleRepository.findByName(adminAR.getName()) == null) {
				roleRepository.save(adminAR);
			}
			Role helpdeskAR = new Role(env.getProperty("auth.authority.helpdesk"));
			if (roleRepository.findByName(helpdeskAR.getName()) == null) {
				roleRepository.save(helpdeskAR);
			}
			Role userAR     = new Role(env.getProperty("auth.authority.user"));
			if (roleRepository.findByName(userAR.getName()) == null) {
				roleRepository.save(userAR);
			}
			Role salesAR    = new Role(env.getProperty("auth.authority.sales"));
			if (roleRepository.findByName(salesAR.getName()) == null) {
				roleRepository.save(salesAR);
			}

			//////////////////////////////
			commonLogger.info("Add new users to the database");

			/*
			 * New and predefined users.
			 *
			 * User names are unique. We do not attempt to add duplicate entries.
			 * This is an issue with traditional SQL databases if existing entries
			 * are present.
			 */

			User adminAU        = new User(
					"admin",
					passwordEncoder.encode("admin"),
					"admin@fjordtek.com"
					);
			if (userRepository.findByUsername(adminAU.getUsername()) == null) {
				userRepository.save(adminAU);
			}

			User helpdeskAU     = new User(
					"helpdesk",
					passwordEncoder.encode("helpdesk"),
					"helpdesk@fjordtek.com"
					);
			if (userRepository.findByUsername(helpdeskAU.getUsername()) == null) {
				userRepository.save(helpdeskAU);
			}

			User salesManagerAU = new User(
					"salesmanager",
					passwordEncoder.encode("salesmanager"),
					"salesmanager@fjordtek.com"
					);
			if (userRepository.findByUsername(salesManagerAU.getUsername()) == null) {
				userRepository.save(salesManagerAU);
			}

			User userAU         = new User(
					"user",
					passwordEncoder.encode("user"),
					"user@fjordtek.com"
					);
			if (userRepository.findByUsername(userAU.getUsername()) == null) {
				userRepository.save(userAU);
			}

			//////////////////////////////

			/*
			 * New and predefined user roles
			 *
			 * We do not attempt to add duplicate entries.
			 * This is an issue with traditional SQL databases if existing entries
			 * are present.
			 */

			// Add example roles for admin user
			if (userRoleRepository.findByCompositeId(
					userRepository.findByUsername(adminAU.getUsername()).getId(),
					roleRepository.findByName(adminAR.getName()).getId()
					) == null) {
				userRoleRepository.save(new UserRole(adminAU, adminAR));
			}

			if (userRoleRepository.findByCompositeId(
					userRepository.findByUsername(adminAU.getUsername()).getId(),
					roleRepository.findByName(salesAR.getName()).getId()
					) == null) {
				userRoleRepository.save(new UserRole(adminAU, salesAR));
			}

			// Add an example role for helpdesk user
			if (userRoleRepository.findByCompositeId(
					userRepository.findByUsername(helpdeskAU.getUsername()).getId(),
					roleRepository.findByName(helpdeskAR.getName()).getId()
					) == null) {
				userRoleRepository.save(new UserRole(helpdeskAU, helpdeskAR));
			}

			// Add an example role for sales manager
			if (userRoleRepository.findByCompositeId(
					userRepository.findByUsername(salesManagerAU.getUsername()).getId(),
					roleRepository.findByName(salesAR.getName()).getId()
					) == null) {
				userRoleRepository.save(new UserRole(salesManagerAU, salesAR));
			}

			// Add an example role for user
			if (userRoleRepository.findByCompositeId(
					userRepository.findByUsername(userAU.getUsername()).getId(),
					roleRepository.findByName(userAR.getName()).getId()
					) == null) {
				userRoleRepository.save(new UserRole(userAU, userAR));
			}

			//////////////////////////////

			commonLogger.info("Sample roles in the database");
			for (Role role : roleRepository.findAll()) {
				commonLogger.info(role.toString());
			}
			commonLogger.info("Sample users in the database");
			commonLogger.info("**HASHED PASSWORDS ARE PRINTED ONLY FOR DEMO PURPOSES**");
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
	@Profile({"dev", "prod"})
	public CommandLineRunner bookDatabaseRunner(
			BookRepository bookRepository,
			BookHashRepository bookHashRepository,
			CategoryRepository categoryRepository,
			AuthorRepository authorRepository
			) {

		return (args) -> {

			//////////////////////////////
			commonLogger.info("Add new categories to the database");

			/*
			 * New and predefined categories.
			 *
			 * Categories are unique. We do not attempt to add duplicate entries.
			 * This is an issue with traditional SQL databases if existing entries
			 * are present.
			 */

			Category catHorror  = new Category(env.getProperty("bookstore.category.horror"));
			if (categoryRepository.findByName(catHorror.getName()) == null) {
				categoryRepository.save(catHorror);
			}

			Category catFantasy = new Category(env.getProperty("bookstore.category.fantasy"));
			if (categoryRepository.findByName(catFantasy.getName()) == null) {
				categoryRepository.save(catFantasy);
			}
			Category catScifi   = new Category(env.getProperty("bookstore.category.scifi"));
			if (categoryRepository.findByName(catScifi.getName()) == null) {
				categoryRepository.save(catScifi);
			}

			//////////////////////////////
			commonLogger.info("Add new authors to the database");

			/*
			 * New and predefined authors.
			 *
			 * Author names are unique. We do not attempt to add duplicate entries.
			 * This is an issue with traditional SQL databases if existing entries
			 * are present.
			 */

			Author authAngelaC  = new Author("Angela","Carter");
			try {
				authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
					authAngelaC.getFirstName(), authAngelaC.getLastName()
					).get(0);
			} catch (IndexOutOfBoundsException e) {
				authorRepository.save(authAngelaC);
			}

			Author authAndrzejS = new Author("Andrzej","Sapkowski");
			try {
				authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
					authAndrzejS.getFirstName(), authAndrzejS.getLastName()
					).get(0);
			} catch (IndexOutOfBoundsException e) {
				authorRepository.save(authAndrzejS);
			}

			//////////////////////////////
			commonLogger.info("Add new sample books to the database");

			/*
			 * New and predefined books + book hashes.
			 */

			Book bookA = new Book(
					"Bloody Chamber",
					authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
							authAngelaC.getFirstName(), authAngelaC.getLastName()
							).get(0),
					1979,
					"1231231-12",
					new BigDecimal("18.00"),
					categoryRepository.findByName(catHorror.getName()),
					true
					);

			Book bookB = new Book(
					"The Witcher: The Lady of the Lake",
					authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
							authAndrzejS.getFirstName(), authAndrzejS.getLastName()
							).get(0),
					1999,
					"3213221-3",
					new BigDecimal("19.99"),
					categoryRepository.findByName(catFantasy.getName()),
					true
					);

			/*
			 * At first, try to find a defined book by its unique ISBN code.
			 * If not found in the database, save the book.
			 * Otherwise, re-define the book object by fetching existing
			 * book entry from the database, determined by its unique ISBN code.
			 *
			 * This processing is required since we set book hash by relying on
			 * book ID information.
			 */

			if (bookRepository.findByIsbn(bookA.getIsbn()) == null) {
				bookRepository.save(bookA);
			} else {
				bookA = bookRepository.findByIsbn(bookA.getIsbn());
			}

			if (bookRepository.findByIsbn(bookB.getIsbn()) == null) {
				bookRepository.save(bookB);
			} else {
				bookB = bookRepository.findByIsbn(bookB.getIsbn());
			}

			/*
			 *  One-to-one unidirectional relationship
			 *  Both directions for table operations must be considered here.
			 */

			BookHash bookHashA = new BookHash();
			if (bookHashRepository.findByBookId(bookA.getId() ) == null) {
				bookA.setBookHash(bookHashA);
				bookHashA.setBook(bookA);
				bookHashRepository.save(bookHashA);
			}

			BookHash bookHashB = new BookHash();
			if (bookHashRepository.findByBookId(bookB.getId() ) == null) {
				bookB.setBookHash(bookHashB);
				bookHashB.setBook(bookB);
				bookHashRepository.save(bookHashB);
			}

			//////////////////////////////
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
