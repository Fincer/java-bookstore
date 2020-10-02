package com.fjordtek.bookstore.model.book;

/**
*
* @author Pekka Helenius
*/

public interface BookRepositoryCustom {

	void updateWithoutPrice(Book book);

}