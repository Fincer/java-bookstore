// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.dev.endpoint;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import com.fjordtek.bookstore.web.BookStoreTestWebContextBuilder;

/**
 *
 * TODO: N/A
 *
 * @author Pekka Helenius
 */

@ActiveProfiles(
		profiles = { "dev" }
		)
public class StatsrefTest extends BookStoreTestWebContextBuilder {

	@Test
	public void testA_GetStatsrefAsNologin() throws Exception {
		loadPageGet(statsRefUrl, 200);
	}
/*
	@Test
	@WithUserDetails("user")
	public void testB_GetStatsrefAsNormalUser() throws Exception {
		loadPageGet(statsRefUrl, 200);
	}

	@Test
	@WithUserDetails("admin")
	public void testC_GetStatsrefAsAdminUser() throws Exception {
		loadPageGet(statsRefUrl, 200);
	}
*/
}