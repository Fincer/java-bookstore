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
 * <b>Test target</b>: list page, delete page
 *
 * @author Pekka Helenius
 */

@TestMethodOrder(Alphanumeric.class)
public class BookdeleteTest extends BookStoreTestWebContextBuilder {

	@Test
	@WithUserDetails("helpdesk")
	public void testA_DeleteLinkNotPresentAsHelpdeskUser() throws Exception {
		assertThat(
				pageContentsList().contains("href=\"/bookdelete")
				).isEqualTo(false);
	}

	@Test
	@WithUserDetails("user")
	public void testB_DeleteFailsAsNormalUser() throws Exception {
		loadPageGet(env.getProperty("page.url.delete") + "/" + hashId, 302);
		if (bookHashRepository.findByHashId(hashId) == null) {
			throw new Exception();
		}
	}

	@Test
	@WithUserDetails("admin")
	public void testC_DeleteSucceedsAsAdminUser() throws Exception {
		loadPageGet(env.getProperty("page.url.delete") + "/" + hashId, 302);
		if (bookHashRepository.findByHashId(hashId) != null) {
			throw new Exception();
		}
	}

}