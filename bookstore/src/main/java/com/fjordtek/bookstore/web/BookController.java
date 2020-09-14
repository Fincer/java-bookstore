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

@Controller
public class BookController {

	protected static final String landingPageURL      = "index";
	protected static final String bookListPageURL     = "booklist";
	protected static final String bookAddPageURL      = "bookadd";
	protected static final String bookDeletePageURL   = "bookdelete";
	protected static final String bookEditPageURL     = "bookedit";

	private HttpServerLogger     httpServerLogger     = new HttpServerLogger();
	private HttpExceptionHandler httpExceptionHandler = new HttpExceptionHandler();

	@Autowired
	private BookRepository       bookRepository;

	//////////////////////////////
	// LIST PAGE
	@RequestMapping(
			value    = bookListPageURL,
			method   = { RequestMethod.GET, RequestMethod.POST }
			)
	public String defaultWebFormGet(HttpServletRequest requestData, Model dataModel) {

		httpServerLogger.logMessageNormal(
				requestData,
				bookListPageURL + ": " + "HTTPOK"
				);

		dataModel.addAttribute("books", bookRepository.findAll());

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

		httpServerLogger.logMessageNormal(
				requestData,
				bookAddPageURL + ": " + "HTTPOK"
				);

		Book newBook = new Book();
		dataModel.addAttribute("book", newBook);

		if (newBook.getYear() == 0) {
			newBook.setYear(Year.now().getValue());
		}

		return bookAddPageURL;
	}

	@RequestMapping(
			value = bookAddPageURL,
			method = RequestMethod.POST
			)
	public String webFormSaveNewBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResult,
			HttpServletRequest requestData
			) {

		if (bindingResult.hasErrors()) {
			httpServerLogger.commonError("Book add: error " + book.toString(), requestData);
			return bookAddPageURL;
		}

		httpServerLogger.logMessageNormal(
				requestData,
				bookAddPageURL + ": " + "HTTPOK"
				);

		bookRepository.save(book);

		return "redirect:" + bookListPageURL;
	}

	//////////////////////////////
	// DELETE BOOK

	@RequestMapping(
			value  = bookDeletePageURL + "/{id}",
			method = RequestMethod.GET
			)
	public String webFormDeleteBook(
			@PathVariable("id") long bookId,
			HttpServletRequest requestData
			) {

		httpServerLogger.logMessageNormal(
				requestData,
				bookDeletePageURL + ": " + "HTTPOK"
				);

		bookRepository.deleteById(bookId);

		return "redirect:../" + bookListPageURL;
	}

	//////////////////////////////
	// UPDATE BOOK

	@RequestMapping(
			value  = bookEditPageURL + "/{id}",
			method = RequestMethod.GET
			)
	public String webFormEditBook(
			@PathVariable("id") long bookId,
			Model dataModel,
			HttpServletRequest requestData
			) {

		httpServerLogger.logMessageNormal(
				requestData,
				bookEditPageURL + ": " + "HTTPOK"
				);

		Book book = bookRepository.findById(bookId).get();
		dataModel.addAttribute("book", book);

		return bookEditPageURL;
	}

	@RequestMapping(
			value = bookEditPageURL + "/{id}",
			method = RequestMethod.POST
			)
	public String webFormUpdateBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResult,
			HttpServletRequest requestData
			) {

		if (bindingResult.hasErrors()) {
			httpServerLogger.commonError("Book edit: error " + book.toString(), requestData);
			return bookEditPageURL;
		}

		httpServerLogger.logMessageNormal(
				requestData,
				bookEditPageURL + ": " + "HTTPOK"
				);

		bookRepository.save(book);

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
