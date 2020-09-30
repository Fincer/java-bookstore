//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fjordtek.bookstore.model.Book;
import com.fjordtek.bookstore.model.BookHashRepository;
import com.fjordtek.bookstore.model.BookRepository;
import com.fjordtek.bookstore.service.HttpServerLogger;

/**
*
* This class implements a custom JSON-related controller for the bookstore,
* handling requests to a predetermined end points only, using a special prefix
* value in its hard-coded @RequestMapping annotation.
*
* @author Pekka Helenius
*/

@RestController
@RequestMapping("json")
public class BookRestController {

	@Autowired
	private BookRepository       bookRepository;

	@Autowired
	private BookHashRepository   bookHashRepository;
/*
	@Autowired
	private CategoryRepository   categoryRepository;
*/

	// TODO Use single variable reference for all controllers
	private static final String bookListPageView      = "booklist";

	private HttpServerLogger     httpServerLogger     = new HttpServerLogger();

	@RequestMapping(
			value  = "booklist",
			method = RequestMethod.GET
			)
	public @ResponseBody Iterable<Book> getAllBooksRestData(
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		httpServerLogger.log(requestData, responseData);

		return bookRepository.findAll();
	}

	@RequestMapping(
			value  = "book" + "/{hash_id}",
			method = RequestMethod.GET
			)
	public @ResponseBody Optional<Book> getBookRestData(
			@PathVariable("hash_id") String bookHashId,
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		try {

			Long bookId = new Long(bookHashRepository.findByHashId(bookHashId).getBookId());
			httpServerLogger.log(requestData, responseData);
			return bookRepository.findById(bookId);

		} catch (NullPointerException e) {

			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);

			this.redirectToDefaultWebForm(requestData, responseData);
			return null;
		}
	}

	//////////////////////////////
	// REDIRECTS

	@RequestMapping(
			value    = { "*" }
			)
	@ResponseStatus(HttpStatus.FOUND)
    public void redirectToDefaultWebForm(
    		HttpServletRequest requestData,
			HttpServletResponse responseData
			) {
    	responseData.setHeader("Location", "/" + bookListPageView);
    	responseData.setStatus(302);
    	httpServerLogger.log(requestData, responseData);
    }

}