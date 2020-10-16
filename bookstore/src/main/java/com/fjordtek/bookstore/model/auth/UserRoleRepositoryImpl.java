//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.auth;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 * This class implements methods defined in UserRoleRepositoryCustom interface.
 *
 * @author Pekka Helenius
 */

@Transactional
public class UserRoleRepositoryImpl implements UserRoleRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	/*
	 * Find unique USER_ROLE entry by user & role IDs
	 */

	@Override
	public UserRole findByCompositeId(Long userId, Long roleId) {

		TypedQuery<UserRole> query = entityManager.createQuery(
				"SELECT i FROM UserRole i" +
				" WHERE user_id = :user_id" +
				" AND role_id   = :role_id",
				UserRole.class
				);

		query.setParameter("user_id", userId);
		query.setParameter("role_id", roleId);

		try {
			return query.getResultList().get(0);
		} catch (IndexOutOfBoundsException ignored) {
			return null;
		}
	}

	@Override
	public void deleteByCompositeId(Long userId, Long roleId) {

		Query query = entityManager.createQuery(
				"DELETE FROM UserRole" +
				" WHERE user_id = :user_id" +
				" AND role_id   = :role_id"
				);
		query.setParameter("user_id", userId);
		query.setParameter("role_id", roleId);
		query.executeUpdate();
	}

}