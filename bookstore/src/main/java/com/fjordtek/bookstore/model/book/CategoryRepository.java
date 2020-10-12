//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * This interface extends CrudRepository interface, implementing
 * custom methods for a repository containing Category entities.
 *
 * @author Pekka Helenius
 */

//TODO un-hardcode path & itemResourceHel
//
@RepositoryRestResource(
		path            = "categories",
		itemResourceRel = "categories",
		exported        = true
		)
public interface CategoryRepository extends CrudRepository<Category, Long> {

	@RestResource(exported = false)
	Category findByName(@Param("name") String name);

	@RestResource(path = "category", rel = "category")
	Category findByNameIgnoreCaseContaining(@Param("name") String name);

}