//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(
		path            = "booklist",
		itemResourceRel = "booklist",
		exported        = true
		)
public interface BookRepository extends CrudRepository<Book, Long> {

	@Override
	//@RestResource(exported = false)
	public Optional<Book> findById(Long id);

	@RestResource(path = "title", rel = "title")
	public List<Book> findByTitle(@Param("name") String title);

	/* Assume a single book with a single ISBN, or multiple books with possibly duplicate ISBNs?
	 * For meanwhile, we have a UNIQUE constraint for ISBN values. If this policy changes,
	 * this method must be changed, as well. Since database allows only UNIQUE values for ISBNs
	 * we return a single book.
	*/
	//public List<Book> findByIsbn(String isbn);
	@RestResource(exported = false)
	public Book findByIsbn(String isbn);

	@RestResource(exported = false)
	public boolean existsByIsbn(String isbn);

}