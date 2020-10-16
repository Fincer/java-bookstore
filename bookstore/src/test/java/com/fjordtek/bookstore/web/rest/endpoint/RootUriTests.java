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
public class RootUriTests extends BookStoreTestWebContextBuilder {

	@Test
	@WithUserDetails("admin")
	public void testA_getBookListPageAsAdminUser() throws Exception {
		loadPageGet(restApiBaseUrl + env.getProperty("page.url.restapi.books"), 200);
	}

	@Test
	@WithUserDetails("user")
	public void testB_getBookListPageExpectForbiddenAsNormalUser() throws Exception {
		loadPageGet(restApiBaseUrl + env.getProperty("page.url.restapi.books"), 403);
	}



	@Test
	@WithUserDetails("admin")
	public void testC_getAuthorsPageAsAdminUser() throws Exception {
		loadPageGet(restApiBaseUrl + env.getProperty("page.url.restapi.authors"), 200);
	}

	@Test
	public void testD_getAuthorsPageExpectUnauthorizedAsNologin() throws Exception {
		loadPageGet(restApiBaseUrl + env.getProperty("page.url.restapi.authors"), 401);
	}


	@Test
	@WithUserDetails("admin")
	public void testE_getCategoriesPageAsAdminUser() throws Exception {
		loadPageGet(restApiBaseUrl + env.getProperty("page.url.restapi.categories"), 200);
	}

	@Test
	public void testF_getCategoriesPageExpectUnauthorizedAsNologin() throws Exception {
		loadPageGet(restApiBaseUrl + env.getProperty("page.url.restapi.categories"), 401);
	}


	@Test
	@WithUserDetails("admin")
	public void testG_getUsersPageAsAdminUser() throws Exception {
		loadPageGet(restApiBaseUrl + env.getProperty("page.url.restapi.users"), 200);
	}

	@Test
	@WithUserDetails("salesmanager")
	public void testH_getUsersPageExpectForbiddenAsMarketingUser() throws Exception {
		loadPageGet(restApiBaseUrl + env.getProperty("page.url.restapi.users"), 403);
	}



	@Test
	@WithUserDetails("admin")
	public void testI_getRolesPageAsAdminUser() throws Exception {
		loadPageGet(restApiBaseUrl + env.getProperty("page.url.restapi.roles"), 200);
	}

	@Test
	@WithUserDetails("user")
	public void testJ_getRolesPageExpectForbiddenAsNormalUser() throws Exception {
		loadPageGet(restApiBaseUrl + env.getProperty("page.url.restapi.roles"), 403);
	}

}