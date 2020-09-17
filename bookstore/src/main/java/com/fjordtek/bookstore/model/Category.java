//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Category {

	////////////////////
	// Primary key value in database
	@Id
	@GeneratedValue(
			strategy     = GenerationType.AUTO,
			generator    = "categoryIdGenerator"
			)
	@SequenceGenerator(
			name         = "categoryIdGenerator",
			sequenceName = "categoryIdSequence"
			)
    private long id;

	////////////////////
	// Attributes with hard-coded constraints
	private String name;

	@OneToMany(
			mappedBy = "category"
			//fetch    = FetchType.LAZY,
			//cascade  = CascadeType.ALL
			)
	private List<Book> books;

	////////////////////
	// Attribute setters

	public void setId(long id) {
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

	public long getId() {
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
		return "[" + "id: " + this.id + ", " +
				"name: " + this.name + "]";
	}

}