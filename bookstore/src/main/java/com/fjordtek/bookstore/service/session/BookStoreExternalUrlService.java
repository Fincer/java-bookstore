//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service.session;

import java.io.IOException;

/**
*
* This interface defines methods of BookStoreExternalUrlServiceImpl class.
*
* @author Pekka Helenius
*
*/

public interface BookStoreExternalUrlService {

	boolean getUrl(String url) throws IOException;

}