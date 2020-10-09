//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import java.security.SecureRandom;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class implements BookHash entity which forms
 * core structure for the corresponding BOOK_HASH table in a database.
 * <p>
 * Additionally, BookHash entity objects are Java objects having
 * methods, attributes and other class-related additions within them.
 * <p>
 * This entity shares same primary key with the Book entity
 * which is simultaneously a foreign key (unidirectional mapping)
 * for this one.
 * <p>
 * This entity generates a table which holds auto-generated
 * random string values associated to each book.
 * <p>
 * These random string values represent difficult-to-enumerate
 * unique book IDs used for front-end purposes. Main purpose
 * is not to expose real Book entity id value.
 *
 * @see https://www.programmersought.com/article/1610322983/
 *
 * @author Pekka Helenius
 */

@Entity
@Table(name = "BOOK_HASH")
public class BookHash {

	@Id
	@GeneratedValue(
			strategy   =  GenerationType.SEQUENCE,
			generator  = "bookHashIdGenerator"
			)
	@GenericGenerator(
			name       = "bookHashIdGenerator",
			strategy   = "foreign",
			parameters = { @Parameter(name = "property", value = "book") }
			)
	@Column(
			name       = "book_id",
			unique     = true,
			nullable   = false
			)
	@JsonIgnore
	private Long bookId;


	//@MapsId
	@OneToOne(
			cascade      = { CascadeType.MERGE, CascadeType.REMOVE },
			fetch        = FetchType.LAZY,
			mappedBy     = "bookhash",
			targetEntity = Book.class
			)
	@PrimaryKeyJoinColumn(
			referencedColumnName = "id"
			)
    private Book book;

	////////////////////
	// Attribute setters

	/*
	 * Ignore UPDATE queries in Jakarta Persistence API context.
	 *
	 * Security note:
	 *
	 * To actually prevent UPDATE queries, bookstore database user must have
	 * permission denial for UPDATE queries for BOOK_HASH table in SQL database
	 * internal security policy.
	 *
	 * SQL server admin // Remove UPDATE permission:
	 *
	 * REVOKE UPDATE on <database_name>.BOOK_HASH FROM '<bookstore_user>'@'sql-server_domain-name';
	 *
	 * SQL server admin // Confirm changes:
	 *
	 * SHOW GRANTS FOR '<bookstore_user>'@'sql-server_domain-name';

	 * If needed, contact your SQL server admin to configure this policy.
	 */
	@Column(
			name             = "hash_id",
			unique           = true,
			columnDefinition = "CHAR(32)",
			updatable        = false,
			nullable         = false
			)
	@JsonProperty(
			value = "hashid"
			)
	private String hashId;


	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	/*
	 * This setter sets an auto-generated value
	 * No manual intervention.
	 */
	public void setHashId() {

		byte[] byteInit = new byte[16];

		new SecureRandom().nextBytes(byteInit);

		StringBuilder shaStringBuilder = new StringBuilder();

		for (byte b : byteInit) {
			shaStringBuilder.append(String.format("%02x", b));
		}

		this.hashId = shaStringBuilder.toString();

	}

	////////////////////
	// Attribute getters

	public Long getBookId() {
		return bookId;
	}

	public Book getBook() {
		return book;
	}

	public String getHashId() {
		return hashId;
	}

	////////////////////
	// Class constructors

	public BookHash() {
		this.setHashId();
	}

	////////////////////
	// Class overrides

	@Override
	public String toString() {
		return "[" + "book_id: " + this.bookId + ", " +
				"hash_id: " + this.hashId + "]";

	}

}
