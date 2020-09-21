//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import java.math.BigDecimal;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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

	/* We allow both comma and dot decimal separators
	 * for BigDecimal data types used in Book class.
	 */
	@InitBinder("book")
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(BigDecimal.class, new BigDecimalPropertyEditor());
	}

	@Autowired
	private BookRepository       bookRepository;

	@Autowired
	private CategoryRepository   categoryRepository;

	private static final String landingPageView       = "index";
	private static final String bookListPageView      = "booklist";
	private static final String bookAddPageView       = "bookadd";
	private static final String bookDeletePageView    = "bookdelete";
	private static final String bookEditPageView      = "bookedit";

	private Map<String,String> globalModelMap = new HashMap<String,String>() {
		private static final long serialVersionUID = 1L;
	{
		put("indexpage",  landingPageView);
		put("listpage",   bookListPageView);
		put("addpage",    bookAddPageView);
		put("deletepage", bookDeletePageView);
		put("editpage",   bookEditPageView);
	}};

	private HttpServerLogger     httpServerLogger     = new HttpServerLogger();

	@ModelAttribute
	public void globalAttributes(Model dataModel) {

		// Security implications of adding these all controller-wide?
		dataModel.addAllAttributes(globalModelMap);
		dataModel.addAttribute("categories", categoryRepository.findAll());
	}

	//////////////////////////////
	// LIST PAGE
	@RequestMapping(
			value    = bookListPageView,
			method   = { RequestMethod.GET, RequestMethod.POST }
			)
	public String defaultWebFormGet(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Model dataModel
			) {

		dataModel.addAttribute("books", bookRepository.findAll());

		httpServerLogger.log(requestData, responseData);

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
			HttpServletResponse responseData,
			Model dataModel
			) {

		Book newBook = new Book();
		newBook.setYear(Year.now().getValue());
		dataModel.addAttribute("book", newBook);

		httpServerLogger.log(requestData, responseData);

		return bookAddPageView;
	}

	@RequestMapping(
			value    = bookAddPageView,
			method   = RequestMethod.POST
			)
	public String webFormSaveNewBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResult,
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		// TODO consider better solution. Add custom Hibernate annotation for Book class?
		if (bookRepository.existsByIsbn(book.getIsbn())) {
			bindingResult.rejectValue("isbn", "error.user", "ISBN code already exists");
		}

		if (bindingResult.hasErrors()) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);
			return bookAddPageView;
		}

		httpServerLogger.log(requestData, responseData);

		bookRepository.save(book);

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
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		bookRepository.deleteById(bookId);

		httpServerLogger.log(requestData, responseData);

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
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		Book book = bookRepository.findById(bookId).get();
		dataModel.addAttribute("book", book);

		httpServerLogger.log(requestData, responseData);

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
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		// NOTE: We have a unique and non-nullable ISBN value for each book.
		if (bookId != book.getId()) {
			bindingResult.rejectValue("name", "error.user", "Wrong book");
		}

		// TODO consider better solution. Add custom Hibernate annotation for Book class?
		Book bookI = bookRepository.findByIsbn(book.getIsbn());

		// If existing ISBN value is not attached to the current book...
		if (bookI.getId() != book.getId()) {
			bindingResult.rejectValue("isbn", "error.user", "ISBN code already exists");
		}

		if (bindingResult.hasErrors()) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);
			return bookEditPageView;
		}

		bookRepository.save(book);

		httpServerLogger.log(requestData, responseData);

		return "redirect:../" + bookListPageView;
	}

	//////////////////////////////
	// REDIRECTS

	@RequestMapping(
			value    = { "*" }
			)
	@ResponseStatus(HttpStatus.FOUND)
	public String redirectToDefaultWebForm(
			HttpServletRequest requestData,
			HttpServletResponse responseData
			)
	{
		httpServerLogger.log(requestData, responseData);
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
