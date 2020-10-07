//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fjordtek.bookstore.model.book.Book;
import com.fjordtek.bookstore.model.book.BookHashRepository;
import com.fjordtek.bookstore.model.book.BookRepository;
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
@RequestMapping("${page.url.json}")
public class BookRestController {

	@Autowired
	private Environment env;

	@Autowired
	private BookRepository       bookRepository;

	@Autowired
	private BookHashRepository   bookHashRepository;
/*
	@Autowired
	private CategoryRepository   categoryRepository;
*/

	@Autowired
	private HttpServerLogger     httpServerLogger;

	@RequestMapping(
			value  = "${page.url.json.list}",
			method = RequestMethod.GET
			)
	public @ResponseBody Iterable<Book> getAllBooksRestData(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Authentication authData
			) {

		String authorities = authData.getAuthorities().toString();

		httpServerLogger.log(requestData, responseData);

		if (authorities.contains(env.getProperty("auth.authority.sales"))) {
			return bookRepository.findAll();
		} else {
			return bookRepository.findAllPublished();
		}
	}

	@RequestMapping(
			value  = "${page.url.json.book}" + "/{hash_id}",
			method = RequestMethod.GET
			)
	public @ResponseBody Optional<Book> getBookRestData(
			@PathVariable("hash_id") String bookHashId,
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Authentication authData
			) {

		String authorities = authData.getAuthorities().toString();

		try {

			Long bookId = new Long(bookHashRepository.findByHashId(bookHashId).getBookId());

			Book book = bookRepository.findById(bookId).get();

			/*
			 * Prevent other than MARKETING users to access hidden book
			 * data even if they knew hash id.
			 *
			 * In this scenario, a book is invisible but a user still knows book's hash id.
			 * However, he/she has no proper MARKETING authorization
			 * to access the URL so we force him/her out of the page.
			 */
			if ( !book.getPublish() && !authorities.contains(env.getProperty("auth.authority.sales")) ) {
		    	responseData.setHeader("Location", env.getProperty("page.url.index"));
		    	responseData.setStatus(302);
		    	httpServerLogger.log(requestData, responseData);
		    	return null;
			}

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
    	responseData.setHeader("Location", env.getProperty("page.url.index"));
    	responseData.setStatus(302);
    	httpServerLogger.log(requestData, responseData);
    }

}