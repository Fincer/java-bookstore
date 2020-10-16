// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fjordtek.bookstore.model.auth.RoleRepository;
import com.fjordtek.bookstore.model.auth.UserRepository;
import com.fjordtek.bookstore.model.book.Book;
import com.fjordtek.bookstore.model.book.BookHash;
import com.fjordtek.bookstore.model.book.BookHashRepository;
import com.fjordtek.bookstore.model.book.BookRepository;

/**
 * Common web context builder for Bookstore end point tests.
 *
 * @author Pekka Helenius
 */

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = {
				"spring.datasource.url = jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
		}
)
@ContextConfiguration
//@WebAppConfiguration
//@AutoConfigureMockMvc
public class BookStoreTestWebContextBuilder {

	/**
	 *  Not autowired due to a custom Spring security hook
	 *
	 *  @see com.fjordtek.bookstore.config.WebSecurityConfig
	 */
//	@Autowired
	protected MockMvc mockMvc;

	protected Book book;
	protected String hashId;

	protected String bookJsonListUrl;
	protected String bookJsonUrl;

	protected String statsRefUrl;
	protected String restApiBaseUrl;

	@Autowired
	protected Environment env;

    @Autowired
    private WebApplicationContext webApplicationContext;

	@Autowired
	protected UserRepository     userRepository;

	@Autowired
	protected RoleRepository     roleRepository;

	@Autowired
	protected BookRepository     bookRepository;

	@Autowired
	protected BookHashRepository bookHashRepository;

	/*
	 * Add Spring Security integration + web application context
	 * Add common objects.
	 */

	@Before
	public void setupTest() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(springSecurity())
//				.apply(sharedHttpSession())
				.build();

		book    = getFirstBookHash().getBook();
		hashId  = getFirstBookHash().getHashId();

		bookJsonListUrl =
				env.getProperty("page.url.json") + env.getProperty("page.url.json.list");

		bookJsonUrl =
				env.getProperty("page.url.json") + env.getProperty("page.url.json.book") + "/" + hashId;

		statsRefUrl =
				env.getProperty("page.url.dev") + env.getProperty("page.url.dev.statsref");

		restApiBaseUrl =
				env.getProperty("spring.data.rest.base-path");

	}


	protected ResultActions loadPageGet(String page, int status) throws Exception {
		return mockMvc
				.perform(
						get(page)
						)
				.andExpect(
						status().is(status)
						)
				;
	}

	protected ResultActions loadPagePost(
			String page,
			int status,
			MediaType contentType,
			String contentData
			) throws Exception {
		return mockMvc
				.perform(
						post(page).contentType(contentType).content(contentData)
						)
				.andExpect(
						status().is(status)
						)
				;
	}

	protected ResultActions loadPagePut(
			String page,
			int status,
			MediaType contentType,
			String contentData
			) throws Exception {
		return mockMvc
				.perform(
						put(page).contentType(contentType).content(contentData)
						)
				.andExpect(
						status().is(status)
						)
				;
	}

	protected ResultActions loadPageDelete(String page, int status) throws Exception {
		return mockMvc
				.perform(
						delete(page)
						)
				.andExpect(
						status().is(status)
						)
				;
	}

	protected String loadPageContents(String page) throws Exception {
		MvcResult pageResult = mockMvc
		.perform(
				get(page)
				)
		.andReturn()
		;
		return pageResult.getResponse().getContentAsString();
	}

	protected String loadPageContents(String page, int status) throws Exception {
		MvcResult pageResult = mockMvc
				.perform(
						get(page))
				.andExpect(
						status().is(status)
						)
				.andReturn()
				;
		return pageResult.getResponse().getContentAsString();
	}




	private BookHash getFirstBookHash() throws Exception {

		/*
		 * NOTE: Assumes we have at least one book already in the database
		 */

		BookHash bookHash = bookHashRepository.findByBookId(
				bookRepository.findAll().get(0).getId()
		);

		return bookHash;
	}




	protected String pageContentsList() throws Exception {
		return loadPageContents(env.getProperty("page.url.list"));
	}

	protected String pageContentsEdit() throws Exception {
		return loadPageContents(env.getProperty("page.url.edit") + "/" + hashId, 200);
	}

	protected String pageContentsAdd() throws Exception {
		return loadPageContents(env.getProperty("page.url.add"), 200);
	}

	protected String pageContentsApiref() throws Exception {
		return loadPageContents(env.getProperty("page.url.apiref"));
	}

	protected String pageContentsJsonList() throws Exception {
		return loadPageContents(
				env.getProperty("page.url.json") + env.getProperty("page.url.json.list")
				);
	}

}