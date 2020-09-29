//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * This interface extends CrudRepository interface, implementing
 * custom methods for a repository containing BookHash entities.
 *
 * @author Pekka Helenius
 */

@RepositoryRestResource(
		path            = "bookhashes",
		itemResourceRel = "bookhashes",
		exported        = true
		)
public interface BookHashRepository extends CrudRepository<BookHash, String> {

	public BookHash findByHashId(String bookHashId);

	/*
	 * We need to override native delete method.
	 * This is a native query, do not unnecessarily validate it.
	 */
	@Modifying
	@Query(
			value = "DELETE FROM BOOK_HASH i WHERE i.book_id = :bookId",
			nativeQuery = true
			)
	public void deleteByBookId(@Param("bookId") Long bookId);

}