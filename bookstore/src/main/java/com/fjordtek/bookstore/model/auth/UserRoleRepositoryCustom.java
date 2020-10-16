//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.auth;

/**
*
* This interface defines additional methods for UserRoleRepository.
*
* @author Pekka Helenius
*/

public interface UserRoleRepositoryCustom {
	UserRole findByCompositeId(Long userId, Long roleId);

	void deleteByCompositeId(Long userId, Long roleId);
}