//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BookRepository extends CrudRepository<Book, Long> {

	@Override
	public Optional<Book> findById(Long id);

	public List<Book> findByTitle(@Param("title") String title);

	/* Assume a single book with a single ISBN, or multiple books with possibly duplicate ISBNs?
	 * For meanwhile, we have a UNIQUE constraint for ISBN values. If this policy changes,
	 * this method must be changed, as well. Since database allows only UNIQUE values for ISBNs
	 * we return a single book.
	*/
	//public List<Book> findByIsbn(String isbn);
	public Book findByIsbn(String isbn);

	public boolean existsByIsbn(String isbn);

}