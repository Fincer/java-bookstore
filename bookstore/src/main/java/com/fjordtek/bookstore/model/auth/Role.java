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
import javax.persistence.Transient;

/**
 * This class implements Role entity which forms
 * core structure for the corresponding ROLE table in a database.
 * <p>
 * Additionally, Role entity objects are Java objects having
 * methods, attributes and other class-related additions within them.
 *
 * @author Pekka Helenius
 */

@Entity
public class Role {

	////////////////////
	// Primary key value in database

	@Id
	@GeneratedValue(
			strategy     = GenerationType.IDENTITY,
			generator    = "roleIdGenerator"
			)
	@SequenceGenerator(
			name         = "roleIdGenerator",
			sequenceName = "roleIdSequence"
			)
	private Long id;

	@Column(
			unique   = true,
			nullable = false,
			columnDefinition = "VARCHAR(20)"
			)
	private String name;

	@OneToMany(
			mappedBy      = "role",
			cascade       = CascadeType.ALL,
			fetch         = FetchType.LAZY,
			orphanRemoval = true
			)
	// Role objects do not have any UserRoles when initializing them
	@Transient
	private List<UserRole> userRoles;

	////////////////////
	// Attribute setters

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getName() {
		return name;
	}
/*
	public List<UserRole> getUserroles() {
		return userRoles;
	}
*/
	////////////////////
	// Class constructors

	public Role() {
	}

	public Role(String name) {
		// super();
		this.name = name;
	}

	////////////////////
	// Class overrides

	@Override
	public String toString() {
		return "[" + "id: " + this.id   + ", " +
				   "name: " + this.name + "]";
	}

}