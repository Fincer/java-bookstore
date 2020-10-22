//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import java.math.BigDecimal;
import java.time.Year;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fjordtek.bookstore.model.book.AuthorRepository;
import com.fjordtek.bookstore.model.book.Book;
import com.fjordtek.bookstore.model.book.BookEventHandler;
import com.fjordtek.bookstore.model.book.BookHash;
import com.fjordtek.bookstore.model.book.BookHashRepository;
import com.fjordtek.bookstore.model.book.BookRepository;
import com.fjordtek.bookstore.model.book.CategoryRepository;
import com.fjordtek.bookstore.service.BigDecimalPropertyEditor;
import com.fjordtek.bookstore.service.BookAuthorHelper;
import com.fjordtek.bookstore.service.HttpServerLogger;
import com.fjordtek.bookstore.service.session.BookStoreWebRestrictions;

/**
*
* This class implements the default Spring Model View controller for the bookstore.
*
* @author Pekka Helenius
*/

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
	private Environment env;

	@Autowired
	private MessageSource msg;

	@Autowired
	private CategoryRepository   categoryRepository;

	@Autowired
	private AuthorRepository     authorRepository;

	@Autowired
	private BookRepository       bookRepository;

	@Autowired
	private BookHashRepository   bookHashRepository;

	@Autowired
	private HttpServerLogger     httpServerLogger;

	@Autowired
	private BookAuthorHelper     bookAuthorHelper;

	@Autowired
	private BookEventHandler     bookEventHandler;

	@Autowired
	private BookStoreWebRestrictions webRestrictions;

/*
	private Map<String,String> globalModelMap = new HashMap<String,String>() {
		private static final long serialVersionUID = 1L;
	{
		put("foo", Stringbar);
		...
	}};
*/

	@ModelAttribute
	public void globalAttributes(Model dataModel) {

		// Security implications of adding these all controller-wide?
//		dataModel.addAllAttributes(globalModelMap);
		dataModel.addAttribute("categories", categoryRepository.findAll());
		dataModel.addAttribute("authors", authorRepository.findAll());
	}

	//////////////////////////////
	// LIST PAGE
	@RequestMapping(
			value    = "${page.url.list}",
			method   = { RequestMethod.GET, RequestMethod.POST }
			)
	public String defaultWebFormGetPost(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Model dataModel
			) {

		dataModel.addAttribute("books", bookRepository.findAll());
		httpServerLogger.log(requestData, responseData);

		return env.getProperty("page.url.list");
	}

	//////////////////////////////
	// AUTHENTICATION ERROR

	/**
	 * @see com.fjordtek.bookstore.service.session.BookStoreAuthenticationFailureHandler
	 * @see com.fjordtek.bookstore.config.WebSecurityConfig
	 */
	@RequestMapping(
			value   = "${page.url.autherror}",
			method   = RequestMethod.POST
			)
	public String authErrorWebFormPost(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			RedirectAttributes redirectAttributes
			) {

		/*
		 * We get these parameters from BookStoreAuthenticationFailureHandler
		 */
		String authfailure = (String) requestData.getAttribute("authfailure");
		String username    = (String) requestData.getAttribute("username");

		if (!username.trim().isEmpty()) {
			authfailure = authfailure + " (" + username + ")";
		}

		/*
		 * Add authfailure attribute to the model
		 * This attribute is referred in file templates/fragments/loginout.html
		 */
		redirectAttributes.addFlashAttribute("authfailure", authfailure);

		return "redirect:" + env.getProperty("page.url.list");

	}

	//////////////////////////////
	// ADD BOOK

	@PreAuthorize("hasAuthority(@BookAuth.SALES)")
	@RequestMapping(
			value    = "${page.url.add}",
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

		return env.getProperty("page.url.add");
	}

	@PreAuthorize("hasAuthority(@BookAuth.SALES)")
	@RequestMapping(
			value    = "${page.url.add}",
			method   = RequestMethod.POST
			)
	public String webFormSaveNewBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResult,
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			RedirectAttributes redirectAttributes
			) {

		////////////
		/*
		 * Hard-coded book count limit.
		 * Added as we expose all accounts to internet
		 * due to course requirements & demo purposes.
		 *
		 * It is assumed that admin account is exposed, too.
		 *
		 * In real life, this must never be a case!
		 * Instead, we should have a proper admin-only
		 * configuration panel where to set these values.
		 *
		 * On SQL server, consider adding the following
		 * configuration:
		 *
		 * ALTER TABLE BOOK MAX_ROWS=<number>;
		 *
		 * See also: https://dev.mysql.com/doc/refman/5.7/en/table-size-limit.html
		 */
		if (webRestrictions.limitBookMaxCount("prod")) {
			redirectAttributes.addFlashAttribute(
					"bookmaxcount",
					msg.getMessage(
							"security.book.count.max.msg",
							null,
							"security.book.count.max.msg [placeholder]",
							requestData.getLocale()
						)
					+ " " + env.getProperty("security.book.count.max") + "."
					);

			return "redirect:" + env.getProperty("page.url.list");
		}


		// TODO consider better solution. Add custom Hibernate annotation for Book class?
		if (bookRepository.existsByIsbn(book.getIsbn())) {
			bindingResult.rejectValue(
					"isbn",
					"error.user",
					msg.getMessage(
							"book.error.isbn.exists",
							null,
							"book.error.isbn.exists [placeholder]",
							requestData.getLocale()
						)
					);
		}

		if (bindingResult.hasErrors()) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);
			return env.getProperty("page.url.add");
		}

		httpServerLogger.log(requestData, responseData);

		bookAuthorHelper.detectAndSaveUpdateAuthorForBook(book);

		bookRepository.save(book);

		// Manually call a book event handler. Is there a better way to do this?
		bookEventHandler.handleAfterCreate(book);

		return "redirect:" + env.getProperty("page.url.list");
	}

	//////////////////////////////
	// DELETE BOOK

	@Transactional
	@PreAuthorize("hasAuthority(@BookAuth.ADMIN)")
	@RequestMapping(
			value    = "${page.url.delete}" + "/{hash_id}",
			method   = RequestMethod.GET
			)
	public String webFormDeleteBook(
			@PathVariable("hash_id") String bookHashId,
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		try {
			Long bookId = new Long(bookHashRepository.findByHashId(bookHashId).getBookId());

			/*
			 * Delete associated book id foreign key (+ other row data) from book hash table
			 * at first, after which delete the book.
			 */
			bookHashRepository.deleteByBookId(bookId);
			bookRepository.deleteById(bookId);

		} catch (NullPointerException e) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		httpServerLogger.log(requestData, responseData);

		return "redirect:" + env.getProperty("page.url.list");
	}

	//////////////////////////////
	// UPDATE BOOK

	@PreAuthorize("hasAnyAuthority(@BookAuth.SALES, @BookAuth.HELPDESK)")
	@RequestMapping(
			value    = "${page.url.edit}" + "/{hash_id}",
			method   = RequestMethod.GET
			)
	public String webFormEditBook(
			@PathVariable("hash_id") String bookHashId,
			Model dataModel,
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Authentication authData
			) {

		String authorities = authData.getAuthorities().toString();

		try {
			Long bookIdFromHash = bookHashRepository.findByHashId(bookHashId).getBookId();
			Book book = bookRepository.findById(bookIdFromHash).get();

			dataModel.addAttribute("book", book);

			/*
			 * Prevent other than MARKETING users to access hidden book
			 * data even if they knew hash id.
			 *
			 * In this scenario, a book is invisible but a user still knows book's hash id.
			 * However, he/she has no proper MARKETING authorization
			 * to access the URL so we force him/her out of the page.
			 */
			if ( !book.getPublish() && !authorities.contains(env.getProperty("auth.authority.sales")) ) {
				//responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return "redirect:" + env.getProperty("page.url.list");
			}

			httpServerLogger.log(requestData, responseData);
			return env.getProperty("page.url.edit");

		} catch (NullPointerException e) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);
			return "redirect:" + env.getProperty("page.url.list");
		}

	}

	/* NOTE: We keep Id here for the sake of proper URL formatting.
	 * Keep URL even if the POST request has invalid data.
	 * Do actual modifications by the book *object*, though.
	 * Internally, we never use URL id as a reference for user modifications,
	 * but just as an URL end point.
	*/
	@PreAuthorize("hasAnyAuthority(@BookAuth.SALES, @BookAuth.HELPDESK)")
	@RequestMapping(
			value    = "${page.url.edit}" + "/{hash_id}",
			method   = RequestMethod.POST
			)
	public String webFormUpdateBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResultBook,
			@ModelAttribute ("hash_id") String bookHashId,
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Authentication authData
			) {

		String authorities = authData.getAuthorities().toString();

		BookHash bookHash = bookHashRepository.findByHashId(bookHashId);

		if (bookHash == null) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);
			return "redirect:" + env.getProperty("page.url.list");
		}

		// One-to-one unidirectional relationship handling
		/*
		 * Must be set. Otherwise, we get TemplateProcessingException
		 * when user puts invalid field values
		 * (Thymeleaf template engine can't handle book.bookHash.hashId).
		 */
		book.setBookHash(bookHash);
		/*
		 * Must be set. Otherwise, we may get merge errors when user
		 * has preceding error inputs in this view.
		 */
		bookHash.setBook(book);

		// TODO consider better solution. Add custom Hibernate annotation for Book class?
		Book bookI = bookRepository.findByIsbn(book.getIsbn());
		Long bookId = bookHash.getBookId();

		if (bookId == null) {
			bindingResultBook.rejectValue(
					"name",
					"error.user",
					msg.getMessage(
							"book.error.unknownbook",
							null,
							"book.error.unknownbook [placeholder]",
							requestData.getLocale()
						)
					);
		} else {

			// If existing ISBN value is not attached to the current book...
			if (bookI != null) {
				if (bookI.getId() != bookId) {
					bindingResultBook.rejectValue(
							"isbn",
							"error.user",
							msg.getMessage(
									"book.error.isbn.exists",
									null,
									"book.error.isbn.exists [placeholder]",
									requestData.getLocale()
								)
							);
				}
			}

			/*
			 *  This is necessary so that Hibernate does not attempt to INSERT data
			 *  but UPDATEs current table data.
			 */
			book.setId(bookId);
		}

		if (bindingResultBook.hasErrors()) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);
			return env.getProperty("page.url.edit");
		}

		/*
		 * Prevent other than MARKETING users to access hidden book
		 * data even if they knew hash id.
		 *
		 * In this scenario, an authenticated user has manually injected publish or price value to
		 * true but has no MARKETING authority. We force him/her out of the page to prevent
		 * unauthorized data manipulation.
		 */
		if (
				( book.getPublish() && !authorities.contains(env.getProperty("auth.authority.sales")) ) ||
				( book.getPrice() != null && !authorities.contains(env.getProperty("auth.authority.sales")) )
				) {
			//responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "redirect:" + env.getProperty("page.url.list");
		}

		/*
		 * More sophisticated methods are required to handle
		 * user input with random letter cases etc. considered
		 */
		//authorRepository.save(book.getAuthor());
		bookAuthorHelper.detectAndSaveUpdateAuthorForBook(book);

		if (authorities.contains(env.getProperty("auth.authority.sales")) ) {
			bookRepository.save(book);
		} else {
			bookRepository.updateWithoutPriceAndWithoutPublish(book);
		}

		httpServerLogger.log(requestData, responseData);
		return "redirect:" + env.getProperty("page.url.list");
	}

	//////////////////////////////
	// API REFERENCE HELP PAGE
	@RequestMapping(
			value    = "${page.url.apiref}",
			method   = { RequestMethod.GET }
			)
	public String webFormRestApiRef(
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {
		httpServerLogger.log(requestData, responseData);
		return env.getProperty("page.url.apiref");
	}

	//////////////////////////////
	// REDIRECTS

	@RequestMapping(
			value    = { "*", "${page.url.error}" }
			)
	@ResponseStatus(HttpStatus.FOUND)
	public String redirectToDefaultWebForm(
			HttpServletRequest requestData,
			HttpServletResponse responseData
			)
	{
		if (requestData.getRequestURI().matches("error")) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		httpServerLogger.log(requestData, responseData);
		return "redirect:" + env.getProperty("page.url.list");
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
