//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

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