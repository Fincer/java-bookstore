package com.fjordtek.bookstore.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(
		path            = "authors",
		itemResourceRel = "authors",
		exported        = true
		)
public interface AuthorRepository extends CrudRepository<Author,Long> {

	@RestResource(path = "author", rel = "author")
	public List<Author> findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
			@Param("firstname") String firstName, @Param("lastname") String lastName
			);

}