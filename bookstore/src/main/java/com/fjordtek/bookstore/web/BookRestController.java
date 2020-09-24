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
import com.fjordtek.bookstore.model.BookRepository;

@RestController
@RequestMapping("json")
public class BookRestController {

	@Autowired
	private BookRepository       bookRepository;
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
			value  = "book" + "/{id}",
			method = RequestMethod.GET
			)
	public @ResponseBody Optional<Book> getBookRestData(
			@PathVariable("id") Long bookId,
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		httpServerLogger.log(requestData, responseData);

		return bookRepository.findById(bookId);
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
    	httpServerLogger.log(requestData, responseData);
    	responseData.setHeader("Location", "/" + bookListPageView);
    	responseData.setStatus(302);
    }

}