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
import com.fjordtek.bookstore.model.CategoryRepository;

@Controller
public class BookController {

	private static final String landingPageView       = "index";
	private static final String bookListPageView      = "booklist";
	private static final String bookAddPageView       = "bookadd";
	private static final String bookDeletePageView    = "bookdelete";
	private static final String bookEditPageView      = "bookedit";

	private HttpServerLogger     httpServerLogger     = new HttpServerLogger();
	//private HttpExceptionHandler httpExceptionHandler = new HttpExceptionHandler();

	@Autowired
	private BookRepository       bookRepository;

	@Autowired
	private CategoryRepository   categoryRepository;

	//////////////////////////////
	// LIST PAGE
	@RequestMapping(
			value    = bookListPageView,
			method   = { RequestMethod.GET, RequestMethod.POST }
			)
	public String defaultWebFormGet(HttpServletRequest requestData, Model dataModel) {

		dataModel.addAttribute("books", bookRepository.findAll());

		dataModel.addAttribute("deletepage", bookDeletePageView);
		dataModel.addAttribute("editpage",   bookEditPageView);
		dataModel.addAttribute("addpage",    bookAddPageView);

		httpServerLogger.logMessageNormal(
				requestData,
				bookListPageView + ": " + "HTTPOK"
				);

		return bookListPageView;

	}

	//////////////////////////////
	// ADD BOOK

	@RequestMapping(
			value    = bookAddPageView,
			method   = { RequestMethod.GET, RequestMethod.PUT }
			)
	public String webFormAddBook(
			HttpServletRequest requestData,
			Model dataModel
			) {

		Book newBook = new Book();
		dataModel.addAttribute("book", new Book());
		dataModel.addAttribute("categories", categoryRepository.findAll());

		dataModel.addAttribute("addpage",    bookAddPageView);
		dataModel.addAttribute("listpage",   bookListPageView);

		if (newBook.getYear() == 0) {
			newBook.setYear(Year.now().getValue());
		}

		httpServerLogger.logMessageNormal(
				requestData,
				bookAddPageView + ": " + "HTTPOK"
				);

		return bookAddPageView;
	}

	@RequestMapping(
			value    = bookAddPageView,
			method   = RequestMethod.POST
			)
	public String webFormSaveNewBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResult,
			Model dataModel,
			HttpServletRequest requestData
			) {

		if (bindingResult.hasErrors()) {
			httpServerLogger.commonError("Book add: error " + book.toString(), requestData);
			return bookAddPageView;
		}

		bookRepository.save(book);

		httpServerLogger.logMessageNormal(
				requestData,
				bookAddPageView + ": " + "HTTPOK"
				);

		return "redirect:" + bookListPageView;
	}

	//////////////////////////////
	// DELETE BOOK

	@RequestMapping(
			value    = bookDeletePageView + "/{id}",
			method   = RequestMethod.GET
			)
	public String webFormDeleteBook(
			@PathVariable("id") Long bookId,
			HttpServletRequest requestData
			) {

		bookRepository.deleteById(bookId);

		httpServerLogger.logMessageNormal(
				requestData,
				bookDeletePageView + ": " + "HTTPOK"
				);

		return "redirect:../" + bookListPageView;
	}

	//////////////////////////////
	// UPDATE BOOK

	@RequestMapping(
			value    = bookEditPageView + "/{id}",
			method   = RequestMethod.GET
			)
	public String webFormEditBook(
			@PathVariable("id") Long bookId,
			Model dataModel,
			HttpServletRequest requestData
			) {

		Book book = bookRepository.findById(bookId).get();
		dataModel.addAttribute("book", book);
		dataModel.addAttribute("categories", categoryRepository.findAll());

		dataModel.addAttribute("listpage",   bookListPageView);

		httpServerLogger.logMessageNormal(
				requestData,
				bookEditPageView + ": " + "HTTPOK"
				);

		return bookEditPageView;
	}

	/* NOTE: We keep Id here for the sake of proper URL formatting.
	 * Keep URL even if the POST request has invalid data.
	 * Do actual modifications by the book *object*, though.
	 * Internally, we never use URL id as a reference for user modifications,
	 * but just as an URL end point.
	*/
	@RequestMapping(
			value    = bookEditPageView + "/{id}",
			method   = RequestMethod.POST
			)
	public String webFormUpdateBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResult,
			Model dataModel,
			@PathVariable("id") Long bookId,
			HttpServletRequest requestData
			) {

		bookId = book.getId();
		dataModel.addAttribute("categories", categoryRepository.findAll());

		if (bindingResult.hasErrors()) {
			httpServerLogger.commonError("Book edit: error " + book.toString(), requestData);
			return bookEditPageView;
		}

		bookRepository.save(book);

		httpServerLogger.logMessageNormal(
				requestData,
				bookEditPageView + ": " + "HTTPOK"
				);

		return "redirect:../" + bookListPageView;
	}

	//////////////////////////////
	// REDIRECTS

	@RequestMapping(
			value    = { "/", landingPageView },
			method   = RequestMethod.GET
			)
	@ResponseStatus(HttpStatus.FOUND)
	public String redirectToDefaultWebForm() {
		return "redirect:" + bookListPageView;
	}

	// Other URL requests
	@RequestMapping(
			value    = "*"
			)
	public String errorWebForm(HttpServletRequest requestData) {
		//return httpExceptionHandler.notFoundErrorHandler(requestData);
		return "redirect:" + bookListPageView;
	}

	@RequestMapping(
			value    = "favicon.ico",
			method   = RequestMethod.GET
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
