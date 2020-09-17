//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import javax.persistence.Column;

//import java.sql.Timestamp;
//import javax.validation.constraints.PastOrPresent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fjordtek.bookstore.annotation.CurrentYear;

@Entity
public class Book {

	private static final int strMin         = 2;
	private static final int strMax         = 100;
	// We format length check in Size annotation, not here
	private static final String regexCommon = "^[a-zA-Z0-9\\-\\s]*$";

	private static final int strIsbnFirstPartMin = 7;
	private static final int strIsbnFirstPartMax = 7;
	private static final int strIsbnLastPartMin  = 1;
	private static final int strIsbnLastPartMax  = 3;
	private static final int strIsbnMin = strIsbnFirstPartMin + strIsbnLastPartMin + 1;
	private static final int strIsbnMax = strIsbnFirstPartMax + strIsbnLastPartMax + 1;

	// We format length and syntax here for ISBN input string
	// 1231234-1 <--> 1231234-123
	private static final String regexIsbn = "^[0-9]{" + strIsbnFirstPartMin + "," + strIsbnFirstPartMax + "}" +
			"\\-[0-9]{" + strIsbnLastPartMin + "," + strIsbnLastPartMax + "}$";

	private static final int yearMin     = 1800;
	private static final String minPrice = "0.01";
	private static final String maxPrice = "999.99";

	/* For future reference
	public static final enum BookDataTypes {
		STRING,
		BOOLEAN,
		INTEGER,
		DOUBLE,
		LONG
	}
	*/

	////////////////////
	// Primary key value in database

	@Id
	@GeneratedValue(
			strategy     = GenerationType.AUTO,
			generator    = "bookIdGenerator"
			)
	@SequenceGenerator(
			name         = "bookIdGenerator",
			sequenceName = "bookIdSequence"
			)
    private Long id;

	////////////////////
	// Attributes with hard-coded constraints

	//////////
	@Size(
			min = strMin, max = strMax,
			message = "Title length must be " + strMin + "-" + strMax + " characters"
			)
	@NotBlank(
			message = "Fill the book title form"
			)
	@Pattern(
			regexp  = regexCommon,
			message = "Invalid characters"
			)
	private String title;

	//////////
	@Size(
			min = strMin, max = strMax,
			message = "Author length must be " + strMin + "-" + strMax + " characters"
			)
	@NotBlank(
			message = "Fill the book author form"
			)
	@Pattern(
			regexp  = regexCommon,
			message = "Invalid characters"
			)
	private String author;

	//////////
	// TODO: Prefer Timestamp data type
	// @DateTimeFormat(pattern = "yyyy")
	// private Timestamp     year;
	// ...

	@Min(
			value   = yearMin,
			message = "Minimum allowed year: " + yearMin
			)
	@CurrentYear
	private int year;

	//////////
	@Column(
			unique  = true
			)
	@NotBlank(
			message = "Fill the ISBN code form"
			)
	@Pattern(
			regexp  = regexIsbn,
			message = "Please use syntax: <" +
			strIsbnFirstPartMin + " integers>-<" +
			strIsbnLastPartMin + "-" +
			strIsbnLastPartMax + " integers>"
			)
	@Size(
			min     = strIsbnMin, max = strIsbnMax,
			message = "Length must be " + strIsbnMin + "-" + strIsbnMax + " characters"
			)
	private String isbn;

	//////////
	@NumberFormat(style = Style.NUMBER, pattern = "#,###.###")
	@Digits(
			integer = 3, fraction = 2,
			message = "Invalid price, possibly too many decimals"
			)
	@DecimalMin(
			value   = minPrice, message = "Too low price value. Minimum allowed: " + minPrice
			)
	@DecimalMax(
			value   = maxPrice, message = "Too high price value. Maximum allowed: " + maxPrice
			)
	// TODO: Use BigDecimal to keep exact precision?
	private double price;

	@ManyToOne(
			//fetch    = FetchType.LAZY,
			optional = false
			)
    @JoinColumn(
    		name     = "category_id",
    		nullable = false
    		)
	private Category category;

	////////////////////
	// Attribute setters

	// TODO consider accessibility restrictions?

	// NOTE: in default scenario, this is automatically generated
	public void setId(Long id) {
		this.id = id;
	}

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

	public void setCategory(Category category) {
		this.category = category;
	}

	////////////////////
	// Attribute getters

	// TODO consider accessibility restrictions?

	public Long getId() {
		return id;
	}

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

	public Category getCategory() {
		return category;
	}

	////////////////////
	// Class constructors

	public Book() {}

	public Book(String title, String author, int year, String isbn, double price, Category category) {
		// super();
	    this.title    = title;
	    this.author   = author;
	    this.year     = year;
	    this.isbn     = isbn;
	    this.price    = price;
	    this.category = category;
	}

	////////////////////
	// Class overrides

	@Override
	public String toString() {
		return "[" + "id: "     + this.id     + ", " +
			   "title: "  + this.title  + ", " +
			   "author: " + this.author + ", " +
			   "year: "   + this.year   + ", " +
			   "isbn: "   + this.isbn   + ", " +
			   "price: "  + this.price  + ", " +
			   "category: " + this.category + "]";
	}

}
