//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;

//import java.sql.Timestamp;
//import javax.validation.constraints.PastOrPresent;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fjordtek.bookstore.annotation.CurrentYear;

/**
 * This class implements Book entity which forms
 * core structure for the corresponding BOOK table in a database.
 * <p>
 * Additionally, Book entity objects are Java objects having
 * methods, attributes and other class-related additions within them.
 *
 * @author Pekka Helenius
 */

@Entity
@Table(name = "BOOK")
public class Book {

	private static final int strMin         = 2;
	private static final int strMax         = 100;
	// We format length check in Size annotation, not here
	private static final String regexCommon = "^[a-zA-Z0-9\\-:\\s]*$";

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
			strategy     = GenerationType.IDENTITY,
			generator    = "bookIdGenerator"
			)
	@SequenceGenerator(
			name         = "bookIdGenerator",
			sequenceName = "bookIdSequence"
			)
	@JsonIgnore
    private Long id;

	////////////////////
	// Random hash id purely for front-end purposes
	// Difficult to enumerate

	@JsonIgnore
	@OneToOne(
			fetch        = FetchType.LAZY,
			cascade      = CascadeType.ALL,
			targetEntity = BookHash.class
			)
	@PrimaryKeyJoinColumn
	private BookHash bookhash;

	////////////////////
	// Attributes with hard-coded constraints

	//////////
	@Column(
			nullable = false,
			columnDefinition = "NVARCHAR(" + strMax + ")"
			)
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
	// If category is null, we do not print it in JSON output.
	@JsonUnwrapped

	/*
	 * There are many ways to filter which category fields we want to JSON output.
	 * Using a custom JSON serializer is one of them.
	 */
	@JsonSerialize(using = AuthorJsonSerializer.class)
	@ManyToOne(
			fetch        = FetchType.EAGER,
			optional     = true,
			targetEntity = Author.class
			)
    @JoinColumn(
    		name     = "author_id",
    		nullable = true
    		)
	/*
	 *  Trigger nested (web form) constraint validation when
	 *  this entity object is referred from another entity
	 *  object.
	 */
	@Valid
	private Author author;

	//////////
	// TODO: Prefer Timestamp data type
	// @DateTimeFormat(pattern = "yyyy")
	// private Timestamp     year;
	// ...
	// TODO: Consider allowing 0 value if year is not known
	@Column(
			nullable = false,
			columnDefinition = "INT(4)"
			)
	@Min(
			value   = yearMin,
			message = "Minimum allowed year: " + yearMin
			)
	@CurrentYear
	private int year;

	//////////
	@Column(
			unique  = true,
			nullable = false
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
	@Column(nullable = false)
	@Digits(
			integer = 3, fraction = 2,
			message = "Invalid price value"
			)
	@DecimalMin(
			value   = minPrice, message = "Too low price value. Minimum allowed: " + minPrice
			)
	@DecimalMax(
			value   = maxPrice, message = "Too high price value. Maximum allowed: " + maxPrice
			)
	private BigDecimal price;

	/*
	 * "@JsonManagedReference only works on one-to-many cases, and not many-to-many.
	 * For general-purpose handling of (possibly) cyclic dependencies, @JsonIdentityInfo may be used."
	 *
	 * Ref: https://github.com/FasterXML/jackson-databind/issues/2191#issuecomment-618087892
	 */
	//@JsonManagedReference(value = "category")

	// If category is null, we do not print it in JSON output.
	@JsonUnwrapped

	/*
	 * There are many ways to filter which category fields we want to JSON output.
	 * Using a custom JSON serializer is one of them.
	 */
	@JsonSerialize(using = CategoryJsonSerializer.class)
	@ManyToOne(
			fetch        = FetchType.EAGER,
			optional     = true,
			targetEntity = Category.class
			)
    @JoinColumn(
    		name     = "category_id",
    		nullable = true
    		)
	private Category category;

	/*
	 * TODO should this be a separate Entity with one-to-one relationship?
	 */
	@Column(
			nullable = false,
			columnDefinition = "BIT"
			)
	/*
	 * We allow writing this attribute value as JSON data (POST/PUT requests)
	 * while ignore outputting it in JSON output (GET requests)
	 *
	 * Basic scenario: allow updating via REST API while preventing to access
	 * it, for instance, when requesting book JSON data for reading.
	 */
	@JsonProperty(
			access = Access.WRITE_ONLY
			)
	private boolean publish;

	////////////////////
	// Attribute setters

	// TODO consider accessibility restrictions?

	// NOTE: in default scenario, this is automatically generated
	public void setId(Long id) {
		this.id = id;
	}
/*
	@JsonProperty(
			value = "bookhash"
			)
*/
	public void setBookHash(BookHash bookhash) {
		this.bookhash = bookhash;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	////////////////////
	// Attribute getters

	// TODO consider accessibility restrictions?

	public Long getId() {
		return id;
	}

	@JsonIgnore
	public BookHash getBookHash() {
		return bookhash;
	}

	public String getTitle() {
		return title;
	}

	public Author getAuthor() {
		return author;
	}

	public int getYear() {
		return year;
	}

	public String getIsbn() {
		return isbn;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Category getCategory() {
		return category;
	}

	public boolean getPublish() {
		return publish;
	}

	////////////////////
	// Class constructors

	public Book() {}

	public Book(String title, Author author, int year, String isbn, BigDecimal price, Category category, boolean publish) {
		// super();
	    this.title    = title;
	    this.author   = author;
	    this.year     = year;
	    this.isbn     = isbn;
	    this.price    = price;
	    this.category = category;
	    this.publish  = publish;
	}

	////////////////////
	// Class overrides

	@Override
	public String toString() {
		return "[" + "id: "     + this.id       + ", " +
				"bookhash_id: " + this.bookhash + ", " +
				   "title: "    + this.title    + ", " +
				   "author: "   + this.author   + ", " +
				   "year: "     + this.year     + ", " +
				   "isbn: "     + this.isbn     + ", " +
				   "price: "    + this.price    + ", " +
				   "category: " + this.category + ", " +
				   "publish: "  + this.publish + "]";
	}

}
