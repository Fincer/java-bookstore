// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.rest.endpoint;

import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import com.fjordtek.bookstore.web.BookStoreTestWebContextBuilder;

/**
 *
 * TODO: N/A
 *
 * @author Pekka Helenius
 */

public class RestDeleteTest extends BookStoreTestWebContextBuilder {

	@Test
	@WithUserDetails("user")
	public void testA_deleteExistingBookExpectForbiddenAsNormalUser() throws Exception {
		loadPageDelete(
				restApiBaseUrl + env.getProperty("page.url.restapi.books") + "/2", 403
				);
	}

	@Test
	@WithUserDetails("admin")
	public void testB_deleteExistingBookAsAdminUser() throws Exception {
		loadPageDelete(
				restApiBaseUrl + env.getProperty("page.url.restapi.books") + "/2", 204
				);
	}
/*
	@Test
	@WithUserDetails("admin")
	public void testC_deleteExistingUserRoleAsAdminUser() throws Exception {
		//
		loadPageDelete(
				restApiBaseUrl + env.getProperty("page.url.restapi.userroles") + "/[user_id: 3, role_id: 4]", 204
				);
	}
*/
}