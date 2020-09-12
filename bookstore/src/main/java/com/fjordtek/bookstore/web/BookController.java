//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;

import com.fjordtek.bookstore.model.*;

@Controller
public class BookController {

	
	
	protected static final String landingPageURL      = "index";
	protected static final String bookListPageURL     = "booklist";
	protected static final String bookAddPageURL      = "bookadd";
	protected static final String bookDeletePageURL   = "bookdelete";
	protected static final String bookSavePageURL     = "booksave";

	private HttpServerLogger     httpServerLogger     = new HttpServerLogger();
	private HttpExceptionHandler httpExceptionHandler = new HttpExceptionHandler();

	@Autowired
	private BookRepository       bookRepository;

 @RequestMapping(
         value    = { bookListPageURL },
         method   = { RequestMethod.GET, RequestMethod.POST }
 )
 public String defaultWebFormGet(Model dataModel, HttpServletRequest requestData) {

     httpServerLogger.logMessageNormal(
             requestData,
             "HTTPOK"
     );

     dataModel.addAttribute("books", bookRepository.findAll());
     
     return bookListPageURL;

 }

 @RequestMapping(
		 value    = bookAddPageURL,
		 method   = { RequestMethod.GET, RequestMethod.PUT }
 )
 public String webFormAddBook(Model dataModel, HttpServletRequest requestData) {
	 
     httpServerLogger.logMessageNormal(
             requestData,
             "HTTPOK"
     );
	 
	 dataModel.addAttribute("book", new Book());
	 
     return bookAddPageURL;
 }
 
 @RequestMapping(
		 value = bookSavePageURL,
		 method = RequestMethod.POST
 )
 public String webFormSaveBook(Book book, HttpServletRequest requestData) {

     httpServerLogger.logMessageNormal(
             requestData,
             "HTTPOK"
     );

     bookRepository.save(book);
     
     return "redirect:" + bookListPageURL;
 }
 
 @RequestMapping(
		 value  = bookDeletePageURL + "/{id}",
		 method = RequestMethod.GET
 )
 public String webFormDeleteBook(
		 @PathVariable("id") long bookId,
		 Model dataModel, HttpServletRequest requestData
	) {
	 
     httpServerLogger.logMessageNormal(
             requestData,
             "HTTPOK"
     );
	 
 	bookRepository.deleteById(bookId);
 	
    return "redirect:../" + bookListPageURL;
 }
 
 
 // Redirect
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
 
}
