// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.dev;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fjordtek.bookstore.web.BookDevController;

/**
 *
 * TODO: N/A
 *
 * @author Pekka Helenius
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(
		profiles = { "dev" }
		)
public class BookDevControllerTest {

	@Autowired
	private BookDevController bookDevController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(bookDevController).isNotNull();
	}
}