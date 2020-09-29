//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class implements Author entity which forms
 * core structure for the corresponding AUTHOR table in a database.
 * <p>
 * Additionally, Author entity objects are Java objects having
 * methods, attributes and other class-related additions within them.
 *
 * @author Pekka Helenius
 */

@Entity
public class Author {

	private static final int strMin         = 2;
	private static final int strMax         = 100;
	// We format length check in Size annotation, not here
	private static final String regexCommon = "^[a-zA-Z0-9\\-:\\s]*$";

	////////////////////
	// Primary key value in database

	@Id
	@GeneratedValue(
			strategy     = GenerationType.IDENTITY,
			generator    = "authorIdGenerator"
			)
	@SequenceGenerator(
			name         = "authorIdGenerator",
			sequenceName = "authorIdSequence"
			)
	@JsonIgnore
    private Long id;

	//////////
	@Column(
			name = "firstname",
			nullable = false,
			columnDefinition = "NVARCHAR(" + strMax + ")"
			)
	@Size(
			min = strMin, max = strMax,
			message = "First name length must be " + strMin + "-" + strMax + " characters"
			)
	@NotBlank(
			message = "Fill the first name form"
			)
	@Pattern(
			regexp  = regexCommon,
			message = "Invalid characters"
			)
	private String firstName;

	//////////
	@Column(
			name = "lastname",
			nullable = false,
			columnDefinition = "NVARCHAR(" + strMax + ")"
			)
	@Size(
			min = strMin, max = strMax,
			message = "Last name length must be " + strMin + "-" + strMax + " characters"
			)
	@NotBlank(
			message = "Fill the first name form"
			)
	@Pattern(
			regexp  = regexCommon,
			message = "Invalid characters"
			)
	private String lastName;

	// Omit from Jackson JSON serialization
	//@JsonBackReference(value = "books")
	@JsonIgnore

	@OneToMany(
			mappedBy      = "author",
			fetch         = FetchType.LAZY,
			cascade       = CascadeType.ALL,
			targetEntity  = Book.class
			//orphanRemoval = true
			)
	private List<Book> books;

	////////////////////
	// Attribute setters

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		// Delete leading & trailing whitespaces (typos from user)
		this.firstName = firstName.trim();
	}

	public void setLastName(String lastName) {
		// Delete leading & trailing whitespaces (typos from user)
		this.lastName = lastName.trim();
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	////////////////////
	// Attribute getters

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public List<Book> getBooks() {
		return books;
	}

	////////////////////
	// Class constructors

	public Author() {}

	public Author(String firstName, String lastName) {
		// super();
	    this.firstName  = firstName;
	    this.lastName   = lastName;
	}

	////////////////////
	// Class overrides

	@Override
	public String toString() {
		return "[" + "id: "   + this.id        + ", " +
			   "firstname: "  + this.firstName + ", " +
			   "lastname: "   + this.lastName  + "]";
	}

}