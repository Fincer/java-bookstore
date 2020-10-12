//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * This interface extends CrudRepository interface, implementing
 * custom methods for a repository containing Role entities.
 *
 * @author Pekka Helenius
 */

//TODO un-hardcode path & itemResourceHel
//
@RepositoryRestResource(
		path            = "roles",
		itemResourceRel = "roles",
		exported        = true
		)
public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(String string);

}