// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * TODO: N/A
 *
 * @author Pekka Helenius
 */

@RunWith(SpringRunner.class)
@DataJpaTest
@TestMethodOrder(Alphanumeric.class)
public class BookRepositoriesTest {

	@Autowired
	private BookRepository     bookRepository;

	@Autowired
	private BookHashRepository bookHashRepository;

	@Autowired
	private AuthorRepository   authorRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	//////////////////////////////

	@Test
	public void testA_CreateAuthor() {
		Author authorA = authorRepository.save(new Author("Foo", "Bar"));
		assertThat(authorA).isNotNull();
	}

	@Test
	public void testB_CreateAuthorWithoutFirstName() {
		Author authorA = authorRepository.save(new Author(null, "Bar"));
		assertThat(authorA).isNotNull();
	}

	@Test
	public void testC_CreateAuthorWithoutLastName() {
		Author authorA = authorRepository.save(new Author("Foo", null));
		assertThat(authorA).isNotNull();
	}

	@Test
	public void testD_CreateAuthorDuplicateNameShouldPass() {
		authorRepository.save(new Author("Foo", "Bar"));
		authorRepository.save(new Author("Foo", "Bar"));

		int savedAuthorsCount =
				authorRepository
				.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
						"Foo", "Bar"
						).size();

		assertThat(savedAuthorsCount).isEqualTo(2);
	}

	//////////////////////////////

	@Test
	public void testE_CreateCategory() {
		Category categoryA = categoryRepository.save(new Category("FOO"));
		assertThat(categoryA).isNotNull();
	}

	@Test
	public void testF_CreateCategoryWithoutNameShouldFail() {

		assertThrows(
				ConstraintViolationException.class, () -> {
					categoryRepository.save(new Category(null));
					categoryRepository.findAll().forEach(System.out::print);
				});

	}

	@Test
	public void testG_CreateCategoryDuplicateNameShouldFail() {

		assertThrows(
				DataIntegrityViolationException.class, () -> {
					categoryRepository.save(new Category("BAR"));
					categoryRepository.save(new Category("BAR"));

					categoryRepository.findAll().forEach(System.out::print);
				});

	}

	//////////////////////////////

	@Test
	public void testH_CreateBookWithAuthor() {

		Author authorA = authorRepository.save(new Author("Fan", "Tasy"));

		Book bookA = bookRepository.save(new Book(
				"Bloody Chamber",
				authorA,
				1979,
				"4343434-14",
				new BigDecimal("18.00"),
				null,
				true
				)
				);

		assertThat(bookRepository.findById(bookA.getId())).isNotNull();
	}

	@Test
	public void testI_CreateNewBookWithoutAuthor() {

		Book bookA = bookRepository.save(new Book(
				"Bloody Chamber",
				null,
				1979,
				"4343434-15",
				new BigDecimal("18.00"),
				null,
				true
				)
				);

		assertThat(bookRepository.findById(bookA.getId())).isNotNull();

	}

	@Test
	public void testJ_CreateBookWithCategory() {

		Category categoryA = categoryRepository.save(new Category("Technology"));

		Book bookA = bookRepository.save(new Book(
				"Innovations in the era of information technology",
				null,
				1954,
				"7575757-15",
				new BigDecimal("43.22"),
				categoryA,
				true
				)
				);

		assertThat(bookRepository.findById(bookA.getId())).isNotNull();

	}
	/*
	@Test
	public void testK_CreateBookWithoutCategory() {

	}
	 */
	//////////////////////////////

	@Test
	public void testL_ExistingBookIsbnIsUnique() {

		assertThrows(
				DataIntegrityViolationException.class, () -> {

					bookRepository.save(new Book(
							"Exoplanets and Earth",
							null,
							2009,
							"7575757-15",
							new BigDecimal("43.22"),
							null,
							true
							)
							);

					bookRepository.save(new Book(
							"Interstellar research for astrobiological life",
							null,
							2006,
							"7575757-15",
							new BigDecimal("11.99"),
							null,
							false
							)
							);

					bookRepository.findAll().forEach(System.out::print);
				});

	}

	//////////////////////////////

	@Test
	public void testM_BookHashCanBeCreated() {

		Book bookA = new Book(
				"Exoplanets and Earth",
				null,
				2009,
				"9191919-15",
				new BigDecimal("43.22"),
				null,
				true
				);

		BookHash bookHashA = new BookHash();
		bookHashA.setBook(bookA);
		bookA.setBookHash(bookHashA);

		bookRepository.save(bookA);
		bookHashRepository.save(bookHashA);

		assertThat(bookA).isNotNull();
		assertThat(bookHashA).isNotNull();

		assertThat(bookHashA.getBookId() == bookA.getId()).isEqualTo(true);
		assertThat(bookHashA.getHashId().length() == 32).isEqualTo(true);

		assertThat(bookHashA.getHashId().toString().matches("^[a-z0-9]*$")).isEqualTo(true);

		assertThat(bookHashRepository.findByBookId(bookA.getId())).isNotNull();
		assertThat(bookHashRepository.findByHashId(bookHashA.getHashId())).isNotNull();
	}

	@Test
	public void testN_BookHashCanSetNewBookHashId() {

		Book bookA = new Book(
				"Exoplanets and Earth",
				null,
				2009,
				"9191919-16",
				new BigDecimal("43.22"),
				null,
				true
				);

		BookHash bookHashA = new BookHash();
		bookHashA.setBook(bookA);
		bookA.setBookHash(bookHashA);

		bookRepository.save(bookA);
		bookHashRepository.save(bookHashA);

		String hashIdA = bookHashA.getHashId();
		bookHashA.setHashId();
		String hashIdB = bookHashA.getHashId();

		bookHashRepository.save(bookHashA);

		assertThat(bookHashA.getHashId().equals(hashIdA)).isEqualTo(false);

	}

	@Test
	public void testO_BookHashCanNotDeleteBookId() {

		assertThrows(
				JpaSystemException.class, () -> {
					Book bookA = new Book(
							"Exoplanets and Earth",
							null,
							2009,
							"9191919-17",
							new BigDecimal("43.22"),
							null,
							true
							);

					BookHash bookHashA = new BookHash();
					bookHashA.setBook(bookA);
					bookA.setBookHash(bookHashA);

					bookRepository.save(bookA);

					bookHashA.setBookId(null);
					bookHashRepository.save(bookHashA);
					bookHashRepository.findAll().forEach(System.out::print);
				});

	}

	//////////////////////////////

	@Test
	public void testP_DeleteAuthorBookHasAuthorConstraintShouldFail() {

		assertThrows(
				DataIntegrityViolationException.class, () -> {
					Author authorA = authorRepository.save(new Author("Hor", "Ror"));

					bookRepository.save(new Book(
							"Twisted adventures in Elvenwood treehouse",
							authorA,
							2006,
							"7575757-15",
							new BigDecimal("11.99"),
							null,
							false
							)
							);

					authorRepository.delete(authorA);
					bookRepository.findAll().forEach(System.out::print);
				});
	}

	@Test
	public void testQ_DeleteCategoryBookHasCategoryConstraintShouldFail() {

		assertThrows(
				DataIntegrityViolationException.class, () -> {
					Category categoryA = categoryRepository.save(new Category("Mystery"));

					bookRepository.save(new Book(
							"Ruins of Rkund",
							null,
							2009,
							"7575757-15",
							new BigDecimal("11.99"),
							categoryA,
							true
							)
							);

					categoryRepository.delete(categoryA);
					bookRepository.findAll().forEach(System.out::print);
				});
	}

}