//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * This interface extends CrudRepository interface, implementing
 * custom methods for a repository containing Author entities.
 *
 * @author Pekka Helenius
 */

//TODO un-hardcode path & itemResourceHel
//
@RepositoryRestResource(
		path            = "authors",
		itemResourceRel = "authors",
		exported        = true
		)
public interface AuthorRepository extends CrudRepository<Author,Long> {

	@RestResource(path = "fullname", rel = "fullname")
	List<Author> findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
			@Param("firstname") String firstName, @Param("lastname") String lastName
			);

	@RestResource(path = "firstname", rel = "firstname")
	List<Author> findByFirstNameIgnoreCaseContaining(
			@Param("firstname") String firstName
			);

	@RestResource(path = "lastname", rel = "lastname")
	List<Author> findByLastNameIgnoreCaseContaining(
			@Param("lastname") String lastName
			);

}