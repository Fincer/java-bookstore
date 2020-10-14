// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.webform.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Arrays;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.security.test.context.support.WithUserDetails;

import com.fjordtek.bookstore.web.BookStoreTestWebContextBuilder;

/**
 * Web application end point test
 * <p>
 * <b>Test target</b>: list page, log in, log out
 *
 * @author Pekka Helenius
 */

@TestMethodOrder(Alphanumeric.class)
public class BooklistTest extends BookStoreTestWebContextBuilder {

	@Test
	public void testA_CsrfTokenExists() throws Exception {
		mockMvc.perform(
				post(env.getProperty("page.url.list"))
				.with(csrf())
				);
	}

	@Test
	public void testB_ExternalFooterLoads() throws Exception {
		assertThat(pageContentsList().contains("footer-items"))
		.isEqualTo(true);
	}

	@Test
	public void testC_LoginFormExists() throws Exception {

		String[] loginKeywords = {
				"id=\"bookstore-loginout\"",
				"login-submit",
				"username",
				"password"
				};

		assertThat(
				Arrays.stream(loginKeywords).allMatch(pageContentsList()::contains)
				).isEqualTo(true);

	}

	@Test
	public void testD_AuthFailureOccurs() throws Exception {

		mockMvc
		.perform(
				formLogin(env.getProperty("page.url.list"))
				.loginProcessingUrl(env.getProperty("page.url.login"))
				.user(
						env.getProperty("auth.field.username"), "foo"
						)
				.password(
						env.getProperty("auth.field.password"), "bar"
						)
				)
		.andExpect(unauthenticated());
	}
/*
	@Test
	public void testAuthFailureOccursFormCharacterOverflow() throws Exception {

		int charCount = 100000;
		byte[] bytes = new byte[charCount];

		new Random().nextBytes(bytes);

		StringBuilder shaStringBuilder = new StringBuilder();

		for (byte b : bytes) {
			shaStringBuilder.append(String.format("%02x", b));
		}

		String inputString = shaStringBuilder.toString();

		mockMvc
		.perform(
				formLogin(env.getProperty("page.url.list"))
				.loginProcessingUrl(env.getProperty("page.url.login"))
				.user(
						env.getProperty("auth.field.username"), inputString
						)
				.password(
						env.getProperty("auth.field.password"), inputString
						)
				)
		.andExpect(unauthenticated());
	}
*/
	@Test
	public void testE_AuthSuccessOccurs() throws Exception {

		mockMvc
		.perform(
				formLogin(env.getProperty("page.url.list"))
				.loginProcessingUrl(env.getProperty("page.url.login"))
				.user(
						env.getProperty("auth.field.username"), "admin"
						)
				.password(
						env.getProperty("auth.field.password"), "admin"
						)
				)
		.andExpect(authenticated());
	}

	@Test
	@WithUserDetails("salesmanager")
	public void testF_BookListIsPresentAsMarketingUser() throws Exception {
		assertThat(
				pageContentsList().contains("id=\"booklist\"")
				).isEqualTo(true);
	}

	@Test
	@WithUserDetails("user")
	public void testG_BookListIsPresentAsNormalUser() throws Exception {
		assertThat(
				pageContentsList().contains("id=\"booklist\"")
				).isEqualTo(true);
	}

	@Test
	public void testH_BookListIsNotPresentAsNologin() throws Exception {
		assertThat(
				pageContentsList().contains("id=\"booklist\"")
				).isEqualTo(false);
	}


	@Test
	@WithUserDetails("salesmanager")
	public void testI_LogoutSucceedsAsMarketingUser() throws Exception {
		mockMvc.perform(
				logout()
				.logoutUrl(env.getProperty("page.url.logout"))
				);
	}


}
