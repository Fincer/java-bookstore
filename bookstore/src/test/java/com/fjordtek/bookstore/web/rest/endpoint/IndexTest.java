// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.rest.endpoint;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.security.test.context.support.WithUserDetails;

import com.fjordtek.bookstore.web.BookStoreTestWebContextBuilder;

/**
 *
 * TODO: N/A
 *
 * @author Pekka Helenius
 */

@TestMethodOrder(Alphanumeric.class)
public class IndexTest extends BookStoreTestWebContextBuilder {

	@Test
	@WithUserDetails("admin")
	public void testA_getIndexPageExpectRedirectAsAdminUser() throws Exception {
		loadPageGet(restApiBaseUrl, 302);
	}

	@Test
	public void testB_getIndexPageExpectUnauthorizedAsNologin() throws Exception {
		loadPageGet(restApiBaseUrl, 401);
	}

}