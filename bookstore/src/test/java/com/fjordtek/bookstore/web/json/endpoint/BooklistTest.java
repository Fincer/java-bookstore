// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.json.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.security.test.context.support.WithUserDetails;

import com.fjordtek.bookstore.web.BookStoreTestWebContextBuilder;

/**
 * Web application end point test
 * <p>
 * <b>Test target</b>: book JSON data list page
 *
 * @author Pekka Helenius
 */

@TestMethodOrder(Alphanumeric.class)
public class BooklistTest extends BookStoreTestWebContextBuilder {

	@Test
	public void testA_NotGetJsonListAsNologin() throws Exception {
		/*
		 * Expect redirect, not 403
		 * Unauthorized users: we pretend the resource (end point) does not exist at all
		 */
		loadPageGet(bookJsonListUrl, 302);
	}

	@Test
	@WithUserDetails("user")
	public void testB_GetJsonListAsNormalUser() throws Exception {
		loadPageGet(bookJsonListUrl, 200);
	}

	@Test
	@WithUserDetails("admin")
	public void testC_GetHiddenJsonListAsAdminUser() throws Exception {
		book.setPublish(false);
		bookRepository.save(book);

		/*
		 *  Is book ISBN present?
		 */
		assertThat(
				pageContentsJsonList().contains(book.getIsbn().toString())
				).isEqualTo(true);

	}

	@Test
	@WithUserDetails("user")
	public void testD_NotGetHiddenJsonListAsNormalUser() throws Exception {
/*
		book.setPublish(false);
		bookRepository.save(book);
*/
		/*
		 *  Is book ISBN present?
		 */
		assertThat(
				pageContentsJsonList().contains(book.getIsbn().toString())
				).isEqualTo(false);

	}

}