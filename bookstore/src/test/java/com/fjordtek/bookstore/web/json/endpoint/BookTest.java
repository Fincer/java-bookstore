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
 * <b>Test target</b>: single book JSON data page
 *
 * @author Pekka Helenius
 */

@TestMethodOrder(Alphanumeric.class)
public class BookTest extends BookStoreTestWebContextBuilder {

	@Test
	public void testA_NotGetJsonAsNologin() throws Exception {
		book.setPublish(true);
		bookRepository.save(book);
		/*
		 * Expect redirect, not 403
		 * Unauthorized users: we pretend the resource (end point) does not exist at all
		 */
		loadPageGet(bookJsonUrl, 302);
	}

	@Test
	@WithUserDetails("user")
	public void testB_GetJsonAsNormalUser() throws Exception {
/*
		book.setPublish(true);
		bookRepository.save(book);
*/
		loadPageGet(bookJsonUrl, 200);
	}

	@Test
	@WithUserDetails("admin")
	public void testC_GetHiddenJsonAsAdminUser() throws Exception {
		book.setPublish(false);
		bookRepository.save(book);

		/*
		 *  Is book ISBN present?
		 */
		assertThat(
				loadPageContents(bookJsonUrl, 200).contains(book.getIsbn().toString())
				).isEqualTo(true);
	}

	@Test
	@WithUserDetails("user")
	public void testD_NotGetHiddenJsonAsNormalUser() throws Exception {
/*
		book.setPublish(false);
		bookRepository.save(book);
*/
		/*
		 * Expect redirect, not 403
		 * Unauthorized users: we pretend the resource (end point) does not exist at all
		 */
		loadPageGet(bookJsonUrl, 302);
	}

}