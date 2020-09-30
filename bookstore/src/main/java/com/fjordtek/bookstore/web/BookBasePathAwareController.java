//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fjordtek.bookstore.model.book.Book;
import com.fjordtek.bookstore.model.book.BookEventHandler;
import com.fjordtek.bookstore.model.book.BookRepository;
import com.fjordtek.bookstore.model.book.CategoryRepository;
import com.fjordtek.bookstore.service.BookAuthorHelper;
import com.fjordtek.bookstore.service.HttpServerLogger;

/**
 *
 * This class implements a custom Spring REST controller for the bookstore,
 * overriding specific REST API controller end point handlings
 * with @BasePathAwareController annotation.
 * <p>
 * @RepositoryRestController
 * To extend default REST API end point request handling, add a class
 * with this annotation instead.
 *
 * @author Pekka Helenius
 */

@BasePathAwareController
public class BookBasePathAwareController {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	private BookAuthorHelper bookAuthorHelper;
	private BookEventHandler bookEventHandler;

	private HttpServerLogger httpServerLogger = new HttpServerLogger();

	/*
	 * This method MUST exist with Autowired annotation. Handles autowiring of external classes.
	 * If this method is not defined, they are not found by this controller class (are null).
	 */
	@Autowired
	private void instanceAttributeController(
			BookAuthorHelper bookAuthorHelper,
			BookEventHandler bookEventHandler
			) {
		this.bookAuthorHelper = bookAuthorHelper;
		this.bookEventHandler = bookEventHandler;
	}

	//////////////////////////////
	private void bookGetAndSetNestedJSON(Book book, JsonNode bookNode) {
		// Nested data: Determine nested JSON keys & their values

		String authorFirstName = null, authorLastName = null;
		String categoryName = null;

		// We keep going even if some of these are still null
		try { authorFirstName = bookNode.get("author").get("firstname").textValue(); } catch (NullPointerException e) {};
		try { authorLastName  = bookNode.get("author").get("lastname").textValue(); } catch (NullPointerException e) {};
		try { categoryName    = bookNode.get("category").get("name").textValue(); } catch (NullPointerException e) {};

		/*
		 *  Treat nested JSON keys & values in a special way. Since input
		 *  JSON data may lack some information to form a proper object,
		 *  we need to use sophisticated methods to find and set missing
		 *  attribute values properly for the Book entity object.
		 */

		// These must be set anyway, otherwise we get an error.
		book.setAuthor(null);
		book.setCategory(null);

		book.setAuthor(
				bookAuthorHelper.detectAndSaveUpdateAuthorByName(authorFirstName, authorLastName)
				);

		if (categoryName != null) {
			book.setCategory(
					categoryRepository.findByNameIgnoreCaseContaining(categoryName).get(0)
					);
		}
	}

	//////////////////////////////
	@RequestMapping(
			value    = "booklist",
			method   = RequestMethod.POST,
			consumes = "application/json",
			produces = "application/hal+json"
			)
	public ResponseEntity<?> bookAddJSONFormPost(
			@RequestBody JsonNode bookJsonNodeEntity,
			PersistentEntityResourceAssembler bookAssembler,
    		HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		try {

			/*
			 * Map JSON input string to Book entity object.
			 * Nested data is not mapped.
			 */
			ObjectMapper jsonMapper = new ObjectMapper();
			Book bookJ = jsonMapper.readValue(bookJsonNodeEntity.toString(), Book.class);

			this.bookGetAndSetNestedJSON(bookJ, bookJsonNodeEntity);

			// Save book
			bookRepository.save(bookJ);

			// Manually call a book event handler. Is there a better way to do this?
			bookEventHandler.handleAfterCreate(bookJ);

			httpServerLogger.log(requestData, responseData);

			/*
			 * Send JSON HAL response to the client
			 * Return object type: PersistentEntityResource
			 */
			return ResponseEntity.ok(bookAssembler.toFullResource(bookJ));

		} catch (Exception e) {
			e.printStackTrace();

			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(
			value    = "booklist" + "/{id}",
			method   = RequestMethod.PUT,
			consumes = "application/json",
			produces = "application/hal+json"
			)
	public ResponseEntity<?> bookUpdateJSONFormPut(
			@RequestBody JsonNode bookPartialJsonNodeEntity,
			@PathVariable("id") Long bookId,
			PersistentEntityResourceAssembler bookAssembler,
    		HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		try {
			Book bookR = bookRepository.findById(bookId).get();

			ObjectMapper jsonMapper = new ObjectMapper();
			ObjectReader jsonReader = jsonMapper.readerForUpdating(bookR);

			Book bookJ = jsonReader.readValue(bookPartialJsonNodeEntity.toString(), Book.class);

			this.bookGetAndSetNestedJSON(bookJ, bookPartialJsonNodeEntity);

			bookRepository.save(bookJ);

			httpServerLogger.log(requestData, responseData);

			/*
			 * Send JSON HAL response to the client
			 * Return object type: PersistentEntityResource
			 */
			return ResponseEntity.ok(bookAssembler.toFullResource(bookJ));

		} catch (Exception e) {
			e.printStackTrace();

			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
}
