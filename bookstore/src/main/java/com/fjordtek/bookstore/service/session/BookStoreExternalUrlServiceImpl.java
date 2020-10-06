//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.service.session;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

/**
 *
 * This class implements BookStoreExternalUrlService interface,
 * considering a situation where DNS name resolution may fail and
 * we are unable to resolve external host IPs by their domain names.
 * <p>
 * Main motivation is to prevent server-side Thymeleaf process loops or any other
 * unintended behavior caused by such events.
 * <p>
 * Reasons for name resolution failures vary: they can occur due to a faulty
 * DNS server operational states or invalid client's DNS cache, broken websites,
 * changed domain IPs, etc.
 *
 * @author Pekka Helenius
 *
 */

@Component("ExternalUrl")
public class BookStoreExternalUrlServiceImpl implements BookStoreExternalUrlService {

	@Override
	public boolean getUrl(String urlString) throws IOException {

		try {
			URL url = new URL(urlString);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setRequestMethod("HEAD");
			try {
				int responseCode = httpConnection.getResponseCode();

				if (responseCode == HttpURLConnection.HTTP_OK) {
					httpConnection.disconnect();
					return true;
				} else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
					return false;
				}
			} catch (UnknownHostException eu) {
				//eu.printStackTrace();
				//throw eu;
				return false;
			}

			httpConnection.disconnect();
			return false;
		} catch (IOException ei) {
			//ei.printStackTrace();
			//throw ei;
			return false;
		}
	}

}