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
 * <b>Test target</b>: list page, edit page
 *
 * @author Pekka Helenius
 */

@TestMethodOrder(Alphanumeric.class)
public class BookeditTest extends BookStoreTestWebContextBuilder {


	@Test
	@WithUserDetails("user")
	public void testA_EditLinkNotPresentAsNormalUser() throws Exception {
		assertThat(
				pageContentsList().contains("href=\"" + env.getProperty("page.url.edit") )
				).isEqualTo(false);
	}

	@Test
	@WithUserDetails("admin")
	public void testB_EditLinkPresentAsAdminUser() throws Exception {
		assertThat(
				pageContentsList().contains("href=\"" + env.getProperty("page.url.edit") )
				).isEqualTo(true);
	}

	@Test
	@WithUserDetails("admin")
	public void testC_EditFieldsExistAsAdminUser() throws Exception {
		assertThat(
				pageContentsEdit().contains("class=\"bookform-section\"")
				).isEqualTo(true);
	}

	@Test
	public void testD_EditNotPresentAsNologin() throws Exception {
		/*
		 * Expect redirect, not 403
		 * Unauthorized users: we pretend the resource (end point) does not exist at all
		 */
		loadPageGet(env.getProperty("page.url.edit") + "/" + hashId, 302);
	}

	@Test
	@WithUserDetails("salesmanager")
	public void testE_EditPriceIsPresentAsMarketingUser() throws Exception {
		assertThat(
				pageContentsEdit().contains("id=\"price")
				).isEqualTo(true);
	}

	@Test
	@WithUserDetails("salesmanager")
	public void testF_EditPublishIsPresentAsMarketingUser() throws Exception {
		assertThat(
				pageContentsEdit().contains("id=\"publish")
				).isEqualTo(true);
	}

	@Test
	@WithUserDetails("helpdesk")
	public void testG_EditPriceNotPresentAsHelpDeskUser() throws Exception {
		assertThat(
				pageContentsEdit().contains("id=\"price")
				).isEqualTo(false);
	}

	@Test
	@WithUserDetails("helpdesk")
	public void testH_EditPublishNotPresentAsHelpDeskUser() throws Exception {
		assertThat(
				pageContentsEdit().contains("id=\"publish")
				).isEqualTo(false);
	}

	@Test
	@WithUserDetails("admin")
	public void testI_EditFormLoginoutFormIsPresentAsAdmin() throws Exception {
		assertThat(
				pageContentsEdit().contains("id=\"bookstore-loginout\"")
				).isEqualTo(true);
	}

}