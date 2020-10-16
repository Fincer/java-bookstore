//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * This interface extends CrudRepository interface, implementing
 * custom methods for a repository containing Book entities.
 *
 * @author Pekka Helenius
 */

//TODO un-hardcode path & itemResourceHel
//
@RepositoryRestResource(
		path            = "booklist",
		itemResourceRel = "booklist",
		exported        = true
		)
public interface BookRepository extends CrudRepository<Book, Long>, BookRepositoryCustom {

	@Override
	//@RestResource(exported = false)
	Optional<Book> findById(Long id);

	@RestResource(path = "title", rel = "title")
	List<Book> findByTitleIgnoreCaseContaining(@Param("name") String title);

	/* Assume a single book with a single ISBN, or multiple books with possibly duplicate ISBNs?
	 * For meanwhile, we have a UNIQUE constraint for ISBN values. If this policy changes,
	 * this method must be changed, as well. Since database allows only UNIQUE values for ISBNs
	 * we return a single book.
	*/
	//public List<Book> findByIsbn(String isbn);
	@RestResource(exported = false)
	Book findByIsbn(String isbn);

	@RestResource(exported = false)
	boolean existsByIsbn(String isbn);

	@Override
	List<Book> findAll();

	/*
	 * We need to override native delete method due to book hash id usage.
	 * This is a native HQL query, do not unnecessarily validate it.
	 */
	@Override
	@Modifying
	@Query(
			value = "DELETE FROM BOOK WHERE id = :id",
			nativeQuery = true
			)
	void deleteById(@Param("id") Long id);

}