// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
 * This class handles core initialization procedures for the bookstore application.
 * <p>
 * Main role is to add predefined database entries for the bookstore.
 *
 * @author Pekka Helenius
 *
 */

/*
 * TODO any better ways to do separation between profiles rather than doing code-level
 * if checks but avoiding duplicate code?
 * Another consideration is to implement proper sign up form since users can't be added
 * without direct database operations in production environment now.
 */

@Profile({"dev", "prod"})
@Component
public class BookCommandLineRunner implements CommandLineRunner {

	@Autowired private Environment        env;
	@Autowired private UserRepository     userRepository;
	@Autowired private RoleRepository     roleRepository;
	@Autowired private UserRoleRepository userRoleRepository;
	@Autowired private BookRepository     bookRepository;
	@Autowired private BookHashRepository bookHashRepository;
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private AuthorRepository   authorRepository;

	private static final Logger commonLogger = LoggerFactory.getLogger(BookstoreApplication.class);

	private boolean setSamplesInProduction() {

		if ( Arrays.stream(env.getActiveProfiles()).anyMatch(p -> p.contains("prod")) ) {
			return Boolean.parseBoolean(env.getProperty("db.sample.data.enabled"));
		}
		return false;

	}

	@Override
	public void run(String... args) throws Exception {

		/** ROLE **/
		/*
		 * New and predefined roles.
		 *
		 * Role names are unique. We do not attempt to add duplicate entries
		 * This is an issue with traditional SQL databases if existing entries
		 * are present.
		 */

		commonLogger.info("------------------------------");
		commonLogger.info("Setting up user roles");

		Role adminAR    = new Role(env.getProperty("auth.authority.admin"));
		Role helpdeskAR = new Role(env.getProperty("auth.authority.helpdesk"));
		Role userAR     = new Role(env.getProperty("auth.authority.user"));
		Role salesAR    = new Role(env.getProperty("auth.authority.sales"));

		if (roleRepository.findByName(adminAR.getName()) == null) {
			roleRepository.save(adminAR);
		} else {
			commonLogger.info("Found existing role '" + env.getProperty("auth.authority.admin")    + "'");
		}

		if (roleRepository.findByName(helpdeskAR.getName()) == null) {
			roleRepository.save(helpdeskAR);
		} else {
			commonLogger.info("Found existing role '" + env.getProperty("auth.authority.helpdesk") + "'");
		}

		if (roleRepository.findByName(userAR.getName()) == null) {
			roleRepository.save(userAR);
		} else {
			commonLogger.info("Found existing role '" + env.getProperty("auth.authority.user")     + "'");
		}

		if (roleRepository.findByName(salesAR.getName()) == null) {
			roleRepository.save(salesAR);
		} else {
			commonLogger.info("Found existing role '" + env.getProperty("auth.authority.sales")    + "'");
		}

		//////////////////////////////

		if (
				Arrays.stream(env.getActiveProfiles()).anyMatch(p -> p.contains("dev")) ||
				setSamplesInProduction()
				) {
			/** USER **/
			/*
			 * New and predefined users.
			 *
			 * User names are unique. We do not attempt to add duplicate entries.
			 * This is an issue with traditional SQL databases if existing entries
			 * are present.
			 */

			commonLogger.info("------------------------------");
			commonLogger.info("Setting up users");

			/*
			 * Set hash strength to 14 (2^14) + use RNG to randomize generated hash.
			 * Default strength value is 10.
			 */
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(14, new SecureRandom());

			User adminAU        = new User(
					"admin",
					passwordEncoder.encode("admin"),
					"admin@fjordtek.com"
					);
			User helpdeskAU     = new User(
					"helpdesk",
					passwordEncoder.encode("helpdesk"),
					"helpdesk@fjordtek.com"
					);
			User salesManagerAU = new User(
					"salesmanager",
					passwordEncoder.encode("salesmanager"),
					"salesmanager@fjordtek.com"
					);
			User userAU         = new User(
					"user",
					passwordEncoder.encode("user"),
					"user@fjordtek.com"
					);

			if (userRepository.findByUsername(adminAU.getUsername()) == null) {
				userRepository.save(adminAU);
			} else {
				commonLogger.info("Found existing user '" + adminAU.getUsername()        + "'");
			}

			if (userRepository.findByUsername(helpdeskAU.getUsername()) == null) {
				userRepository.save(helpdeskAU);
			} else {
				commonLogger.info("Found existing user '" + helpdeskAU.getUsername()     + "'");
			}

			if (userRepository.findByUsername(salesManagerAU.getUsername()) == null) {
				userRepository.save(salesManagerAU);
			} else {
				commonLogger.info("Found existing user '" + salesManagerAU.getUsername() + "'");
			}

			if (userRepository.findByUsername(userAU.getUsername()) == null) {
				userRepository.save(userAU);
			} else {
				commonLogger.info("Found existing user '" + userAU.getUsername()         + "'");
			}

			/** USER_ROLE **/
			/*
			 * New and predefined user roles
			 *
			 * We do not attempt to add duplicate entries.
			 * This is an issue with traditional SQL databases if existing entries
			 * are present.
			 */

			commonLogger.info("------------------------------");
			commonLogger.info("Setting up roles for the users");

			// Add example roles for admin user
			if (userRoleRepository.findByCompositeId(
					userRepository.findByUsername(adminAU.getUsername()).getId(),
					roleRepository.findByName(adminAR.getName()).getId()
					) == null) {
				userRoleRepository.save(new UserRole(
						userRepository.findByUsername(adminAU.getUsername()),
						roleRepository.findByName(adminAR.getName())
						));
			} else {
				commonLogger.info(
						"Found existing role '" + adminAR.getName() + "' for user '" + adminAU.getUsername() + "'"
						);
			}

			if (userRoleRepository.findByCompositeId(
					userRepository.findByUsername(adminAU.getUsername()).getId(),
					roleRepository.findByName(salesAR.getName()).getId()
					) == null) {
				userRoleRepository.save(new UserRole(
						userRepository.findByUsername(adminAU.getUsername()),
						roleRepository.findByName(salesAR.getName())
						));
			} else {
				commonLogger.info(
						"Found existing role '" + salesAR.getName() + "' for user '" + adminAU.getUsername() + "'"
						);
			}

			// Add an example role for helpdesk user
			if (userRoleRepository.findByCompositeId(
					userRepository.findByUsername(helpdeskAU.getUsername()).getId(),
					roleRepository.findByName(helpdeskAR.getName()).getId()
					) == null) {
				userRoleRepository.save(new UserRole(
						userRepository.findByUsername(helpdeskAU.getUsername()),
						roleRepository.findByName(helpdeskAR.getName())
						));
			} else {
				commonLogger.info(
						"Found existing role '" + helpdeskAR.getName() + "' for user '" + helpdeskAU.getUsername() + "'"
						);
			}

			// Add an example role for sales manager
			if (userRoleRepository.findByCompositeId(
					userRepository.findByUsername(salesManagerAU.getUsername()).getId(),
					roleRepository.findByName(salesAR.getName()).getId()
					) == null) {
				userRoleRepository.save(new UserRole(
						userRepository.findByUsername(salesManagerAU.getUsername()),
						roleRepository.findByName(salesAR.getName())
						));
			} else {
				commonLogger.info(
						"Found existing role '" + salesAR.getName() + "' for user '" + salesManagerAU.getUsername() + "'"
						);
			}

			// Add an example role for user
			if (userRoleRepository.findByCompositeId(
					userRepository.findByUsername(userAU.getUsername()).getId(),
					roleRepository.findByName(userAR.getName()).getId()
					) == null) {
				userRoleRepository.save(new UserRole(
						userRepository.findByUsername(userAU.getUsername()),
						roleRepository.findByName(userAR.getName())
						));
			} else {
				commonLogger.info(
						"Found existing role '" + userAR.getName() + "' for user '" + userAU.getUsername() + "'"
						);
			}
		}


		/** CATEGORY **/
		/*
		 * New and predefined categories.
		 *
		 * Categories are unique. We do not attempt to add duplicate entries.
		 * This is an issue with traditional SQL databases if existing entries
		 * are present.
		 */

		commonLogger.info("------------------------------");
		commonLogger.info("Setting up categories");

		Category catHorror  = new Category(env.getProperty("bookstore.category.horror"));
		Category catFantasy = new Category(env.getProperty("bookstore.category.fantasy"));
		Category catScifi   = new Category(env.getProperty("bookstore.category.scifi"));

		if (categoryRepository.findByName(catHorror.getName()) == null) {
			categoryRepository.save(catHorror);
		} else {
			commonLogger.info("Found existing category '" + env.getProperty("bookstore.category.horror")  + "'");
		}


		if (categoryRepository.findByName(catFantasy.getName()) == null) {
			categoryRepository.save(catFantasy);
		} else {
			commonLogger.info("Found existing category '" + env.getProperty("bookstore.category.fantasy") + "'");
		}

		if (categoryRepository.findByName(catScifi.getName()) == null) {
			categoryRepository.save(catScifi);
		} else {
			commonLogger.info("Found existing category '" + env.getProperty("bookstore.category.scifi")   + "'");
		}


		//////////////////////////////

		if (
				Arrays.stream(env.getActiveProfiles()).anyMatch(p -> p.contains("dev")) ||
				setSamplesInProduction()
				) {
			/** AUTHOR **/
			/*
			 * New and predefined authors.
			 *
			 * Author names are unique. We do not attempt to add duplicate entries.
			 * This is an issue with traditional SQL databases if existing entries
			 * are present.
			 */

			commonLogger.info("------------------------------");
			commonLogger.info("Setting up authors");

			Author authAngelaC  = new Author("Angela","Carter");
			Author authAndrzejS = new Author("Andrzej","Sapkowski");

			try {
				authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
						authAngelaC.getFirstName(), authAngelaC.getLastName()
						).get(0);
				commonLogger.info("Found existing author '" +
						authAngelaC.getFirstName() + " " + authAngelaC.getLastName() +
						"'");
			} catch (IndexOutOfBoundsException e) {
				authorRepository.save(authAngelaC);
			}

			try {
				authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
						authAndrzejS.getFirstName(), authAndrzejS.getLastName()
						).get(0);
				commonLogger.info("Found existing author '" +
						authAndrzejS.getFirstName() + " " + authAndrzejS.getLastName() +
						"'");
			} catch (IndexOutOfBoundsException e) {
				authorRepository.save(authAndrzejS);
			}

			/** BOOK **/
			/*
			 * New and predefined books.
			 */

			/*
			 * At first, try to find a defined book by its unique ISBN code.
			 * If not found in the database, save the book.
			 * Otherwise, re-define the book object by fetching existing
			 * book entry from the database, determined by its unique ISBN code.
			 *
			 * This processing is required since we set book hash by relying on
			 * book ID information.
			 */

			commonLogger.info("------------------------------");
			commonLogger.info("Setting up sample books");

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

			if (bookRepository.findByIsbn(bookA.getIsbn()) == null) {
				bookRepository.save(bookA);
			} else {
				bookA = bookRepository.findByIsbn(bookA.getIsbn());
				commonLogger.info("Found existing book '" + bookA.getTitle() + "(ISBN: " + bookA.getIsbn() + ")'");
			}

			if (bookRepository.findByIsbn(bookB.getIsbn()) == null) {
				bookRepository.save(bookB);
			} else {
				bookB = bookRepository.findByIsbn(bookB.getIsbn());
				commonLogger.info("Found existing book '" + bookB.getTitle() + "(ISBN: " + bookB.getIsbn() + ")'");
			}

			/** BOOK_HASH **/
			/*
			 * Generate hash IDs for books.
			 *
			 * One-to-one unidirectional relationship
			 * Both directions for table operations must be considered here.
			 */

			commonLogger.info("------------------------------");
			commonLogger.info("Setting up hash IDs for the books");

			BookHash bookHashA = new BookHash();
			BookHash bookHashB = new BookHash();

			if (bookHashRepository.findByBookId(bookA.getId() ) == null) {
				bookA.setBookHash(bookHashA);
				bookHashA.setBook(bookA);
				bookHashRepository.save(bookHashA);
			} else {
				commonLogger.info("Hash already set for book '" + bookA.getTitle() + "(ISBN: " + bookA.getIsbn() + ")'");
			}

			if (bookHashRepository.findByBookId(bookB.getId() ) == null) {
				bookB.setBookHash(bookHashB);
				bookHashB.setBook(bookB);
				bookHashRepository.save(bookHashB);
			} else {
				commonLogger.info("Hash already set for book '" + bookB.getTitle() + "(ISBN: " + bookB.getIsbn() + ")'");
			}

		}

		//////////////////////////////
		commonLogger.info("------------------------------");
		commonLogger.info("Roles in the database");
		for (Role role : roleRepository.findAll()) {
			commonLogger.info(role.toString());
		}

		if ( Arrays.stream(env.getActiveProfiles()).anyMatch(p -> p.contains("dev")) ) {
			commonLogger.info("Sample users in the database");
			commonLogger.info("**HASHED PASSWORDS ARE PRINTED ONLY FOR DEMO PURPOSES**");
			for (User user : userRepository.findAll()) {
				commonLogger.info(user.toString());
			}
			commonLogger.info("Sample user roles in the database");
			for (UserRole userRole : userRoleRepository.findAll()) {
				commonLogger.info(userRole.toString());
			}
		} else {
			commonLogger.info("Skipped database operations for sample users.");
		}

		commonLogger.info("------------------------------");
		commonLogger.info("Categories in the database");
		for (Category category : categoryRepository.findAll()) {
			commonLogger.info(category.toString());
		}

		if ( Arrays.stream(env.getActiveProfiles()).anyMatch(p -> p.contains("dev")) ) {
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
		} else {
			commonLogger.info("Skipped database operations for sample authors or books.\nDatabase may contain previously added data.");
		}
		//////////////////////////////
	}
}