//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	private HttpServerLogger     httpServerLogger     = new HttpServerLogger();
	private HttpExceptionHandler httpExceptionHandler = new HttpExceptionHandler();

	@Autowired
	private BookRepository       bookRepository;

 @RequestMapping(
         value    = { bookListPageURL },
         method   = RequestMethod.GET
 )
 public String DefaultWebFormGet(Model dataModel, HttpServletRequest requestData) {

     httpServerLogger.logMessageNormal(
             requestData,
             "HTTPOK"
     );

     dataModel.addAttribute("books", bookRepository.findAll());
     
     return bookListPageURL;

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
 public String ErrorWebForm(HttpServletRequest requestData) {
    return httpExceptionHandler.notFoundErrorHandler(requestData);
 }
 
}
