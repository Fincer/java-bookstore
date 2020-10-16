//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

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

//TODO un-hardcode path & itemResourceHel
//
@RepositoryRestResource(
		path            = "bookhashes",
		itemResourceRel = "bookhashes",
		exported        = false
		)
public interface BookHashRepository extends CrudRepository<BookHash, String> {

	BookHash findByHashId(String bookHashId);

	BookHash findByBookId(@Param("bookid") Long bookId);

	/*
	 * We need to override native delete method.
	 * This is a native query, do not unnecessarily validate it.
	 */
	@Modifying
	@Query(
			value = "DELETE FROM BOOK_HASH WHERE book_id = :bookid",
			nativeQuery = true
			)
	void deleteByBookId(@Param("bookid") Long bookId);

}