package com.fjordtek.bookstore.model.book;

import java.util.List;

/**
*
* @author Pekka Helenius
*/

public interface BookRepositoryCustom {

	void updateWithoutPrice(Book book);

	List<Book> findAllPublished();
}