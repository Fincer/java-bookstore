// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.webform.endpoint;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;

import com.fjordtek.bookstore.web.BookStoreTestWebContextBuilder;

/**
 * Web application end point test
 * <p>
 * <b>Test target</b>: index page, list page
 *
 * @author Pekka Helenius
 */

@TestMethodOrder(Alphanumeric.class)
public class IndexTest extends BookStoreTestWebContextBuilder {

	@Test
	public void testA_RedirectFromIndexPage() throws Exception {
		/*
		 * Expect redirect
		 */
		loadPageGet(env.getProperty("page.url.index"), 302);
	}

}
