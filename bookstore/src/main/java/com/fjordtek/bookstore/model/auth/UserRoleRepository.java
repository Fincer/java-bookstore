//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * This interface extends CrudRepository interface, implementing
 * custom methods for a repository containing UserRole entities.
 *
 * @author Pekka Helenius
 */

//TODO un-hardcode path & itemResourceHel
//
@RepositoryRestResource(
		path            = "userroles",
		itemResourceRel = "userroles",
		exported        = true
		)
public interface UserRoleRepository extends CrudRepository<UserRole, Long>, UserRoleRepositoryCustom {
}