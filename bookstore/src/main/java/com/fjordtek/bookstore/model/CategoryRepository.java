//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(
		path            = "categories",
		itemResourceRel = "categories",
		exported        = true
		)
public interface CategoryRepository extends CrudRepository<Category, Long> {

	@RestResource(exported = false)
	public List<Category> findByName(@Param("name") String name);

	@RestResource(path = "category", rel = "category")
	public List<Category> findByNameIgnoreCaseContaining(@Param("name") String name);

}