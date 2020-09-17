//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import java.time.Year;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fjordtek.bookstore.model.Book;
import com.fjordtek.bookstore.model.BookRepository;
import com.fjordtek.bookstore.model.Category;
import com.fjordtek.bookstore.model.CategoryRepository;

@Controller
public class BookController {

	protected static final String landingPageURL      = "index";
	protected static final String bookListPageURL     = "booklist";
	protected static final String bookAddPageURL      = "bookadd";
	protected static final String bookDeletePageURL   = "bookdelete";
	protected static final String bookEditPageURL     = "bookedit";
	protected static final String bookSavePageURL     = "booksave";

	private HttpServerLogger     httpServerLogger     = new HttpServerLogger();
	private HttpExceptionHandler httpExceptionHandler = new HttpExceptionHandler();

	@Autowired
	private BookRepository       bookRepository;

	@Autowired
	private CategoryRepository   categoryRepository;

	//////////////////////////////
	// LIST PAGE
	@RequestMapping(
			value    = bookListPageURL,
			method   = { RequestMethod.GET, RequestMethod.POST }
			)
	public String defaultWebFormGet(HttpServletRequest requestData, Model dataModel) {

		dataModel.addAttribute("books", bookRepository.findAll());

		httpServerLogger.logMessageNormal(
				requestData,
				bookListPageURL + ": " + "HTTPOK"
				);

		return bookListPageURL;

	}

	//////////////////////////////
	// ADD BOOK

	@RequestMapping(
			value    = bookAddPageURL,
			method   = { RequestMethod.GET, RequestMethod.PUT }
			)
	public String webFormAddBook(
			HttpServletRequest requestData,
			Model dataModel
			) {

		Book newBook = new Book();
		dataModel.addAttribute("book", newBook);
		dataModel.addAttribute("categories", categoryRepository.findAll());

		if (newBook.getYear() == 0) {
			newBook.setYear(Year.now().getValue());
		}

		httpServerLogger.logMessageNormal(
				requestData,
				bookAddPageURL + ": " + "HTTPOK"
				);

		return bookAddPageURL;
	}

	@RequestMapping(
			value = bookAddPageURL,
			method = RequestMethod.POST
			)
	public String webFormSaveNewBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResult,
			Model dataModel,
			HttpServletRequest requestData
			) {

		if (bindingResult.hasErrors()) {
			httpServerLogger.commonError("Book add: error " + book.toString(), requestData);
			return bookAddPageURL;
		}

		bookRepository.save(book);

		httpServerLogger.logMessageNormal(
				requestData,
				bookAddPageURL + ": " + "HTTPOK"
				);

		return "redirect:" + bookListPageURL;
	}

	//////////////////////////////
	// DELETE BOOK

	@RequestMapping(
			value  = bookDeletePageURL + "/{id}",
			method = RequestMethod.GET
			)
	public String webFormDeleteBook(
			@PathVariable("id") Long bookId,
			HttpServletRequest requestData
			) {

		bookRepository.deleteById(bookId);

		httpServerLogger.logMessageNormal(
				requestData,
				bookDeletePageURL + ": " + "HTTPOK"
				);

		return "redirect:../" + bookListPageURL;
	}

	//////////////////////////////
	// UPDATE BOOK

	@RequestMapping(
			value  = bookEditPageURL + "/{id}",
			method = { RequestMethod.GET }
			)
	public String webFormEditBook(
			@PathVariable("id") Long bookId,
			Model dataModel,
			HttpServletRequest requestData
			) {

		Book book = bookRepository.findById(bookId).get();
		Iterable<Category> categories = categoryRepository.findAll();
		dataModel.addAttribute("book", book);
		dataModel.addAttribute("categories", categories);

		httpServerLogger.logMessageNormal(
				requestData,
				bookEditPageURL + ": " + "HTTPOK"
				);

		return bookEditPageURL;
	}

	/* NOTE: We keep Id here for the sake of proper URL formatting.
	 * Keep URL even if the POST request has invalid data.
	 * Do actual modifications by the book *object*, though.
	 * Internally, we never use URL id as a reference for user modifications,
	 * but just as an URL end point.
	*/
	@RequestMapping(
			value  = bookEditPageURL + "/{id}",
			method = RequestMethod.POST
			)
	public String webFormUpdateBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResult,
			@PathVariable("id") Long bookId,
			HttpServletRequest requestData
			) {

		bookId = book.getId();

		if (bindingResult.hasErrors()) {
			httpServerLogger.commonError("Book edit: error " + book.toString(), requestData);
			return bookEditPageURL;
		}

		bookRepository.save(book);

		httpServerLogger.logMessageNormal(
				requestData,
				bookEditPageURL + ": " + "HTTPOK"
				);

		return "redirect:../" + bookListPageURL;
	}

	//////////////////////////////
	// REDIRECTS

	@RequestMapping(
			value  = { "/", landingPageURL },
			method =  RequestMethod.GET
			)
	@ResponseStatus(HttpStatus.FOUND)
	public String redirectToDefaultWebForm() {
		return "redirect:" + bookListPageURL;
	}

	// Other URL requests
	@RequestMapping(
			value  = "*",
			method = { RequestMethod.GET, RequestMethod.POST }
			)
	public String errorWebForm(HttpServletRequest requestData) {
		return httpExceptionHandler.notFoundErrorHandler(requestData);
	}

	@RequestMapping(
			value  = "favicon.ico",
			method = RequestMethod.GET
			)
	@ResponseBody
	public void faviconRequest() {
		/*
		 * We do not offer favicon for this website.
		 * Avoid HTTP status 404, and return nothing
		 * in server response when client requests the icon file.
		 */
	}

}
