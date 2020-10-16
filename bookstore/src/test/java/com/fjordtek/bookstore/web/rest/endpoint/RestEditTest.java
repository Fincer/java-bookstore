// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.rest.endpoint;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import com.fjordtek.bookstore.web.BookStoreTestWebContextBuilder;

/**
 *
 * TODO: N/A
 *
 * @author Pekka Helenius
 */

@TestMethodOrder(Alphanumeric.class)
public class RestEditTest extends BookStoreTestWebContextBuilder {

	@Test
	@WithUserDetails("admin")
	public void testA_putBookEditAsAdminUser() throws Exception {
		loadPagePut(
				restApiBaseUrl + env.getProperty("page.url.restapi.books") + "/2", 200,
				MediaType.APPLICATION_JSON,
				"{\"title\":\"The Witcher: Blood of Elves\"," +
				"\"year\":1999," +
				"\"isbn\":\"3213221-3\"," +
				"\"price\":22.49}"
				);
	}

	@Test
	@WithUserDetails("user")
	public void testB_putBookEditExpectForbiddenAsNormalUser() throws Exception {
		loadPagePut(
				restApiBaseUrl + env.getProperty("page.url.restapi.books") + "/2", 403,
				MediaType.APPLICATION_JSON,
				"{\"title\":\"Root flag captured backdoor H4X3DV4LU3!!\"," +
				"\"year\":1999," +
				"\"isbn\":\"8919312-7\"," +
				"\"price\":9950.49}"
				);
	}

}