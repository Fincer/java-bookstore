//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import java.util.List;

/**
*
* This interface defines additional methods for BookRepository.
*
* @author Pekka Helenius
*/

public interface BookRepositoryCustom {

	void updateWithoutPriceAndWithoutPublish(Book book);

	List<Book> findAllPublished();
}