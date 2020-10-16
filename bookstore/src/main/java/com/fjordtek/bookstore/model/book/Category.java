//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class implements Category entity which forms
 * core structure for the corresponding CATEGORY table in a database.
 * <p>
 * Additionally, Category entity objects are Java objects having
 * methods, attributes and other class-related additions within them.
 *
 * @author Pekka Helenius
 */

@Entity
@Table(name = "CATEGORY")
public class Category {

	////////////////////
	// Primary key value in database
	@Id
	@GeneratedValue(
			strategy     = GenerationType.IDENTITY,
			generator    = "categoryIdGenerator"
			)
	@SequenceGenerator(
			name         = "categoryIdGenerator",
			sequenceName = "categoryIdSequence"
			)
	@JsonIgnore
	private Long id;

	////////////////////
	// Attributes with hard-coded constraints
	@Column(
			nullable = false,
			unique   = true,
			columnDefinition = "NVARCHAR(50)"
			)
	@NotNull
	private String name;

	// Omit from Jackson JSON serialization
	//@JsonBackReference(value = "books")
	@JsonIgnore

	@OneToMany(
			mappedBy     = "category",
			fetch        = FetchType.LAZY,
			cascade      = CascadeType.ALL,
			targetEntity = Book.class
			)
	private List<Book> books;

	////////////////////
	// Attribute setters

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	////////////////////
	// Attribute getters

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Book> getBooks() {
		return books;
	}

	////////////////////
	// Class constructors

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	////////////////////
	// Class overrides

	@Override
	public String toString() {
		return "[" + "id: " + this.id       + ", " +
			   "name: "     + this.name     + "]";
	}

}