//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

//import java.sql.Timestamp;
//import javax.validation.constraints.PastOrPresent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {
	
	////////////////////
	// Primary key value in database

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long          id;
	
	////////////////////
	// Attributes with hard-coded constraints
	
	@NotNull
	@NotEmpty(message = "Please provide a title")
	@Pattern(regexp="^[a-zA-Z0-9-_ ]+$")
	@Size(min = 2, max = 100, message = "Invalid title length")
	private String        title;
	
	@NotNull
	@NotEmpty(message = "Please provide an author name")
	@Pattern(regexp="^[a-zA-Z ]+$")
	@Size(min = 2, max = 100, message = "Invalid author name length")
	private String        author;
	
	@NotNull
	//@PastOrPresent
	//@DateTimeFormat(pattern = "yyyy")
	// TODO: Prefer Timestamp data type
	// private Timestamp     year;
	private int           year;
	
	@NotNull
	@NotEmpty(message = "Please provide an ISBN code for the book")
	@Pattern(regexp="^[a-zA-Z0-9-_ ]+$")
	@Size(min = 15, max = 15)
	private String        isbn;
	
	@NotNull
	@DecimalMin(value = "0.01",   message = "Too low price value" )
	@DecimalMax(value = "999.99", message = "Too high price value")
	// TODO: Use BigDecimal to keep exact precision?
	private double        price;

	////////////////////
	// Attribute setters
	
	// TODO consider accessibility restrictions?
	
	// Id generated automatically
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	////////////////////
	// Attribute getters
	
	// TODO consider accessibility restrictions?
	
	// Id generated automatically
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public int getYear() {
		return year;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public double getPrice() { 
		return price;
	}
	
	////////////////////
	// Class constructors
	
	public Book() {}

	public Book(String title, String author, int year, String isbn, double price) {
		// super();
	    this.title  = title;
	    this.author = author;
	    this.year   = year;
	    this.isbn   = isbn;
	    this.price  = price;
	}
	
	////////////////////
	// Class overrides
	
	@Override
	public String toString() {
		return this.id + ", " + this.title + ", " + this.author + ", " + this.year + ", " + this.isbn + ", " + this.price;
	}
	
	// ...
}
