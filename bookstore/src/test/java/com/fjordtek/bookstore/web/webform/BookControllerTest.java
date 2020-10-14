// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.web.webform;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fjordtek.bookstore.web.BookController;

/**
 *
 * TODO: N/A
 *
 * @author Pekka Helenius
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookControllerTest {

	@Autowired
	private BookController bookController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(bookController).isNotNull();
	}
}