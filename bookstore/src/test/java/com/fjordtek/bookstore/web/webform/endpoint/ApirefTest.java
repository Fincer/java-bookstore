// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.webform.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.security.test.context.support.WithUserDetails;

import com.fjordtek.bookstore.web.BookStoreTestWebContextBuilder;

/**
 * Web application end point test
 * <p>
 * <b>Test target</b>: list page, apiref page
 *
 * @author Pekka Helenius
 */

@TestMethodOrder(Alphanumeric.class)
public class ApirefTest extends BookStoreTestWebContextBuilder {

	@Test
    @WithUserDetails("user")
	public void testA_ApirefLinkNotPresentAsNormalUser() throws Exception {
		assertThat(
				pageContentsList().contains("href=\"" + env.getProperty("page.url.apiref") )
				).isEqualTo(false);
	}

	@Test
	@WithUserDetails("admin")
	public void testB_ApirefLinkPresentAsAdminUser() throws Exception {
		assertThat(
				pageContentsList().contains("href=\"" + env.getProperty("page.url.apiref") )
				).isEqualTo(true);
	}

	@Test
	@WithUserDetails("admin")
	public void testC_ApirefFormFieldsExistAsAdminUser() throws Exception {
		assertThat(
				pageContentsApiref().contains("id=\"apireftable\"")
				).isEqualTo(true);
	}

	@Test
	public void testD_ApirefFormNotPresentAsNologin() throws Exception {
		/*
		 * Expect redirect, not 403
		 * Unauthorized users: we pretend the resource (end point) does not exist at all
		 */
		loadPageGet(env.getProperty("page.url.apiref"), 302);
	}

}
