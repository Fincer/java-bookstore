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
 * <b>Test target</b>: list page, add page
 *
 * @author Pekka Helenius
 */

@TestMethodOrder(Alphanumeric.class)
public class BookaddTest extends BookStoreTestWebContextBuilder {

	@Test
	@WithUserDetails("user")
	public void testA_AddLinkNotPresentAsNormalUser() throws Exception {
		assertThat(
				pageContentsList().contains("href=\"" + env.getProperty("page.url.add") )
				).isEqualTo(false);
	}

	@Test
	@WithUserDetails("admin")
	public void testB_AddLinkPresentAsAdminUser() throws Exception {
		assertThat(
				pageContentsList().contains("href=\"" + env.getProperty("page.url.add") )
				).isEqualTo(true);
	}

	@Test
	@WithUserDetails("admin")
	public void testC_AddFormFieldsExistAsAdminUser() throws Exception {
		loadPageGet(env.getProperty("page.url.add"), 200);
	}

	@Test
	public void testD_AddFormNotPresentAsNologin() throws Exception {
		/*
		 * Expect redirect, not 403
		 * Unauthorized users: we pretend the resource (end point) does not exist at all
		 */
		loadPageGet(env.getProperty("page.url.add"), 302);
	}

	@Test
	@WithUserDetails("salesmanager")
	public void testE_AddPriceIsPresentAsMarketingUser() throws Exception {
		assertThat(
				pageContentsEdit().contains("id=\"price")
				).isEqualTo(true);
	}

	@Test
	@WithUserDetails("salesmanager")
	public void testF_AddPublishIsPresentAsMarketingUser() throws Exception {
		assertThat(
				pageContentsEdit().contains("id=\"publish")
				).isEqualTo(true);
	}

	@Test
	@WithUserDetails("helpdesk")
	public void testG_AddPriceNotPresentAsHelpDeskUser() throws Exception {
		assertThat(
				pageContentsEdit().contains("id=\"price")
				).isEqualTo(false);
	}

	@Test
	@WithUserDetails("helpdesk")
	public void testH_AddPublishNotPresentAsHelpDeskUser() throws Exception {
		assertThat(
				pageContentsEdit().contains("id=\"publish")
				).isEqualTo(false);
	}

	@Test
	@WithUserDetails("admin")
	public void testI_EditFormLoginoutFormIsPresentAsAdmin() throws Exception {
		assertThat(
				pageContentsAdd().contains("id=\"bookstore-loginout\"")
				).isEqualTo(true);
	}

}
