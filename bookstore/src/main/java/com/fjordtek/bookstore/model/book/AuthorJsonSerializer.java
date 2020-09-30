//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.book;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * This class implements Jackson JSON serializer for Author entity class.
 * <p>
 * When writing a JSON output based on a Author entity object, the processing
 * is handled in the way described in this class.
 * <p>
 * Unless specifically instructed, customization defined in this class
 * does not affect native Spring REST API JSON outputs generated
 * by a controller having either @BasePathAwareController
 * or @RepositoryRestController annotation.
 *
 * @author Pekka Helenius
 */

public class AuthorJsonSerializer extends StdSerializer<Author> {
	private static final long serialVersionUID = 5233819344225306443L;

	public AuthorJsonSerializer() {
		this(null);
	}

	public AuthorJsonSerializer(Class<Author> jd) {
		super(jd);
	}

	@Override
	public void serialize(Author author, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
				gen.writeStartObject();
				// Author class Id has JsonIgnore annotation
				//gen.writeFieldId(author.getId());
				gen.writeStringField("firstname", author.getFirstName());
				gen.writeStringField("lastname", author.getLastName());
				gen.writeEndObject();
	}

}