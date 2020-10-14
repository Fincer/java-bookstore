package com.fjordtek.bookstore.web.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fjordtek.bookstore.web.BookRestController;

/**
 *
 * TODO: N/A
 *
 * @author Pekka Helenius
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookRestControllerTest {

	@Autowired
	private BookRestController bookRestController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(bookRestController).isNotNull();
	}
}