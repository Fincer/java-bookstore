//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.auth;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class implements User entity which forms
 * core structure for the corresponding USER table in a database.
 * <p>
 * Additionally, User entity objects are Java objects having
 * methods, attributes and other class-related additions within them.
 *
 * @author Pekka Helenius
 */

@Entity
@Table(name = "USER")
public class User {

	// TODO define universally in a single place
	private static final int strMax         = 100;
	private static final int strMaxPasswd   = 1024;

	////////////////////
	// Primary key value in database

	@Id
	@GeneratedValue(
			strategy     = GenerationType.IDENTITY,
			generator    = "userIdGenerator"
			)
	@SequenceGenerator(
			name         = "userIdGenerator",
			sequenceName = "userIdSequence"
			)
	private Long id;

	////////////////////

	/*
	 *  TODO: add regex validation and other constraints
	 *  if sign up form is implemented.
	 */
	@Column(
			unique   = true,
			nullable = false,
			columnDefinition = "NVARCHAR(" + strMax + ")"
			)
	@NotBlank
	private String username;

	/*
	 *  TODO: add minimum length validation and other constraints
	 *  if sign up form is implemented.
	 */
	@Column(
			nullable = false,
			columnDefinition = "NVARCHAR(" + strMaxPasswd + ")"
			)
	@JsonIgnore
	@NotBlank
	private String password;

	@Email
	@Column(
			nullable = false,
			columnDefinition = "NVARCHAR(" + strMax + ")"
			)
	private String email;

	@OneToMany(
			mappedBy      = "user",
			cascade       = CascadeType.ALL,
			fetch         = FetchType.LAZY,
			orphanRemoval = true
			)
	// User objects do not have any UserRoles when initializing them
	@Transient
	private List<UserRole> userRoles;

	////////////////////
	// Attribute setters

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}
/*
	public void setUserroles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
*/
	////////////////////
	// Attribute getters

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}
/*
	public List<UserRole> getUserroles() {
		return userRoles;
	}
*/
	////////////////////
	// Class constructors

	public User() {
	}

	public User(String username, String password, String email) {
		// super();
		this.username  = username;
		this.password  = password;
		this.email     = email;
	}

	////////////////////
	// Class overrides

	@Override
	public String toString() {
		return "[" + "id: "   + this.id       + ", " +
				"username: "  + this.username + ", " +
				"password: "  + this.password + ", " +
				"email: "     + this.email    + "]";
	}


}