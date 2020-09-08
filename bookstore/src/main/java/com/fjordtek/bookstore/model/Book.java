//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class Book {
	
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
	@PastOrPresent
	@DateTimeFormat(pattern = "yyyy")
	private LocalDate     year;
	
	@NotNull
	@NotEmpty(message = "Please provide an ISBN code for the book")
	@Pattern(regexp="^[a-zA-Z0-9-_ ]+$")
	@Size(min = 15, max = 15)
	private String        isbn;
	
	@NotNull
	@DecimalMin(value = "0.01",   message = "Too low price value" )
	@DecimalMax(value = "999.99", message = "Too high price value")
	// Keep precision
	private BigDecimal    price;

	////////////////////
	// Attribute setters
	
	// TODO consider accessibility restrictions?
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setYear(LocalDate year) {
		this.year = year;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	////////////////////
	// Attribute getters
	
	// TODO consider accessibility restrictions?
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public LocalDate getYear() {
		return year;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public BigDecimal getPrice() { 
		return price;
	}
	
	////////////////////
	// Class constructors
	
	public Book() {
	}
	
	// ...
	
	////////////////////
	// Class overrides
	
	@Override
	public String toString() {
		// TODO
		return "N/A (placeholder)";
	}
	
	// ...
}
