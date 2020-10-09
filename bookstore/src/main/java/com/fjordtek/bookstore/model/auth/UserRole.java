//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.auth;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * This class implements UserRole entity which forms
 * core structure for the corresponding USER_ROLE join table in a database.
 * <p>
 * Main reason for the join table is to allow more sophisticated
 * role policy for users. For instance, a user may have multiple roles like
 * FINANCE and MARKETING, whereas another user may only have MARKETING
 * role. In other words, this back-end implementation allows fine-grained
 * user policy for the web service and better business logic.
 * <p>
 * Primary key of this entity is a composite primary key.
 * <p>
 * This implementation supports having additional columns in the join table
 * if needed, contrary to the direct Spring @ManyToMany annotation.
 * <p>
 * Additionally, UserRole entity objects are Java objects having
 * methods, attributes and other class-related additions within them.
 *
 * @author Pekka Helenius
 */

@Entity
@Table(name = "USER_ROLE")
public class UserRole {

	////////////////////
	// Primary key value in database

	@EmbeddedId
	private UserRoleCompositeKey id = new UserRoleCompositeKey();

	@ManyToOne
	@MapsId("roleId")
	private User user;

	@ManyToOne
	@MapsId("userId")
	private Role role;

	////////////////////
	// Other attributes

	////////////////////
	// Attribute setters

	public void setId(UserRoleCompositeKey id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	////////////////////
	// Attribute getters

	public UserRoleCompositeKey getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Role getRole() {
		return role;
	}

	////////////////////
	// Class constructors

	public UserRole() {
	}

	public UserRole(User user, Role role) {
		//super();
		this.user = user;
		this.role = role;
	}

	////////////////////
	// Class overrides

	@Override
	public String toString() {
		return "[" + "user: " + this.user + ", " +
				"role: "      + this.role + "]";
	}

}