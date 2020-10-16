// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.rest.endpoint;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import com.fjordtek.bookstore.web.BookStoreTestWebContextBuilder;

/**
 *
 * TODO: N/A
 *
 * @author Pekka Helenius
 */

public class RestAddTest extends BookStoreTestWebContextBuilder {

	@Test
	@WithUserDetails("salesmanager")
	public void testA_postAddBookExpectForbiddenAsMarketingUser() throws Exception {
		loadPagePost(
				restApiBaseUrl + env.getProperty("page.url.restapi.books"), 403,
				MediaType.APPLICATION_JSON,
				"{\"title\":\"Halo: The Flood\"," +
				"\"year\":2003," +
				"\"isbn\":\"0345459-210\"," +
				"\"price\":24.99," +
				"\"category\":{\"name\":\"sCi-fI\"}," +
				"\"author\":{\"lastname\":\"Dietz\"}}"
				);
	}

	@Test
	@WithUserDetails("admin")
	public void testB_postAddBookWithCategoryWithAuthorAsAdminUser() throws Exception {
		loadPagePost(
				restApiBaseUrl + env.getProperty("page.url.restapi.books"), 200,
				MediaType.APPLICATION_JSON,
				"{\"title\":\"Halo: The Flood\"," +
				"\"year\":2003," +
				"\"isbn\":\"0345459-210\"," +
				"\"price\":24.99," +
				"\"category\":{\"name\":\"sCi-fI\"}," +
				"\"author\":{\"lastname\":\"Dietz\"}}"
				);
	}

	@Test
	@WithUserDetails("admin")
	public void testC_postAddBookWithoutCategoryWithAuthorAsAdminUser() throws Exception {
		loadPagePost(
				restApiBaseUrl + env.getProperty("page.url.restapi.books"), 200,
				MediaType.APPLICATION_JSON,
				"{\"title\":\"Mass Effect: Retribution\"," +
				"\"year\":2010," +
				"\"isbn\":\"0345520-722\"," +
				"\"price\":29.90," +
//				"\"category\":{\"name\":\"Sci-Fi\"}," +
				"\"author\":{\"firstname\":\"Drew\",\"lastname\":\"Karpyshyn\"}}"
				);
	}

	@Test
	@WithUserDetails("admin")
	public void testD_postAddAuthorAsAdminUser() throws Exception {
		loadPagePost(
				restApiBaseUrl + env.getProperty("page.url.restapi.authors"), 201,
				MediaType.APPLICATION_JSON,
				"{\"firstname\":\"Food\"," +
				"\"lastname\":\"Carter\"}"
				);
	}

	@Test
	@WithUserDetails("helpdesk")
	public void testE_postAddAuthorExpectForbiddenAsHelpdeskUser() throws Exception {
		loadPagePost(
				restApiBaseUrl + env.getProperty("page.url.restapi.authors"), 403,
				MediaType.APPLICATION_JSON,
				"{\"firstname\":\"Jessica\"," +
				"\"lastname\":\"Retina\"}"
				);
	}

	@Test
	@WithUserDetails("helpdesk")
	public void testF_postAddRoleExpectForbiddenAsHelpdeskUser() throws Exception {
		loadPagePost(
				restApiBaseUrl + env.getProperty("page.url.restapi.roles"), 403,
				MediaType.APPLICATION_JSON,
				"{\"name\":\"SUPERADMIN\"}"
				);
	}

	@Test
	@WithUserDetails("admin")
	public void testG_postAddRoleAsAdminUser() throws Exception {
		loadPagePost(
				restApiBaseUrl + env.getProperty("page.url.restapi.roles"), 201,
				MediaType.APPLICATION_JSON,
				"{\"name\":\"ROOT\"}"
				);
	}

}